package org.scofield.claims.claim.storage

import org.scofield.claims.claim.Claim
import org.scofield.claims.claim.hasPermission
import org.scofield.claims.claim.storage.sort_map.SortMap
import org.scofield.claims.claim.storage.sort_map.SortMapPriorityQueueKey
import org.scofield.claims.ext.isOp
import org.scofield.claims.permission.ClaimPermission
import org.scofield.claims.utils.Rect
import org.scofield.claims.utils.intersects

import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.WorldSavePath
import net.minecraft.world.dimension.DimensionType
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.NoSuchElementException
import com.google.gson.Gson
import java.io.FileWriter

typealias ClaimStorage = SortMap<UUID, Claim>

fun ClaimStorage.save(server: MinecraftServer, world: ServerWorld) {
    val saveDir = DimensionType.getSaveDirectory(world.registryKey, server.getSavePath(WorldSavePath.ROOT).toFile())
    val file = File(saveDir, "/data/claims/claims.json")

    file.createNewFile()

    Gson().toJson(this, FileWriter(file))
}

fun ClaimStorage.getClaim(claimId: UUID): Claim? {
    return this.valueMap[claimId]
}

/**
 * Generates a UUID that is guaranteed not to collide.
 *
 * @throws StackOverflowError If the entire UUID space is occupied, or the random number generator
 * keeps generating a conflicted UUID for long enough, the function will exceed its recursion
 * limit and the runtime will throw.
 *
 * @return A guaranteed unique UUID
 *
 * */
fun ClaimStorage.generateUuid(): UUID {
    val uuid = UUID.randomUUID()
    if (this.valueMap.containsKey(uuid)) return this.generateUuid()
    return uuid
}

/**
 * Checks if a claim collides with any other already existing claim.
 *
 * @param claim The claim to check.
 *
 * @return A boolean indicating if there is a collision.
 *
 */
fun ClaimStorage.isClaimConflicted(claim: Claim): Boolean {
    for ((_, key) in this.layerMap) {
        val value = this.valueMap[key]!!
        if (claim.area intersects value.area) return true
    }
    return false
}

/**
 * Gets the colliding claim if it exists.
 *
 * @param playerId Player id to check against the claim's owner.
 * @param claim The claim to check.
 *
 * @return A pair containing the layer depth and colliding claim.
 *
 */
fun ClaimStorage.getCollidingClaim(playerId: UUID, claim: Claim): Pair<Int, UUID>? {
    for ((layer, key) in this.layerMap) {
        val value = this.valueMap[key]!!
        if (claim.area intersects value.area) {
            return if (playerId == value.ownerId) Pair(layer, key) else null
        }
    }
    return null
}

/**
 * Creates a new claim.
 *
 * @throws StackOverflowError If the [generateUuid] function recurses for long enough.
 *
 * @return A boolean indicating if the creation was successful
 */
fun ClaimStorage.createClaim(player: ServerPlayerEntity, area: Rect): Boolean {
    val uuid = this.generateUuid()
    val claim = Claim(player.uuid, area)

    if (this.isClaimConflicted(claim)) {
        return false
    }

    this[uuid] = Pair(0, claim)

    return true
}

/**
 * Creates a new subclaim within a claim.
 *
 * @throws StackOverflowError If the [generateUuid] function recurses for long enough.
 *
 * @return A boolean indicating if the creation was successful.
 */
fun ClaimStorage.createSubclaim(player: ServerPlayerEntity, area: Rect): Boolean {
    val uuid = this.generateUuid()
    val claim = Claim(player.uuid, area)

    val (layer, parentId) = this.getCollidingClaim(player.uuid, claim) ?: return false

    claim.parentClaimId = parentId

    val parentClaim = this.valueMap[parentId]!!
    parentClaim.subclaims.add(uuid)

    this[uuid] = Pair(layer, claim)

    return true
}

/**
 * Internal implementation function for [deleteClaim]
 */
private fun ClaimStorage.deleteClaimImpl(claimId: UUID) {
    // Delete this claim's subclaims recursively
    run {
        val claim = this.valueMap[claimId]!!
        for (subclaim in claim.subclaims) this.deleteClaimImpl(subclaim)
    }

    this.valueMap.remove(claimId)
    this.layerMap.remove(SortMapPriorityQueueKey(0, claimId))
}

fun ClaimStorage.deleteClaim(player: ServerPlayerEntity, claimId: UUID): Boolean {
    // Delete the subclaim item from the parent claim's subclaims list
    run {
        val claim = this.valueMap[claimId]!!

        // Check if the player has permissions to delete the claim
        if (!(player.isOp() || claim.hasPermission(player.uuid, ClaimPermission.DELETE_CLAIM))) {
            return false
        }

        // Delete the subclaim from the parent claim
        val parentId = claim.parentClaimId
        if (parentId != null) {
            val parentClaim = this.valueMap[parentId] ?: throw NoSuchElementException("This error shouldn't happen LOL!")
            parentClaim.subclaims.remove(claimId)
        }
    }

    // Delete this claim's subclaims recursively
    this.deleteClaimImpl(claimId)

    return true
}

