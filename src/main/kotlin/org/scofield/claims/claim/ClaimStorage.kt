package org.scofield.claims.claim

import net.minecraft.server.network.ServerPlayerEntity
import org.scofield.claims.claim.sort_map.SortMap
import org.scofield.claims.claim.sort_map.SortMapPriorityQueueKey
import org.scofield.claims.utils.Rect
import org.scofield.claims.utils.intersects

import java.util.*
import kotlin.NoSuchElementException

typealias ClaimStorage = SortMap<UUID, Claim>

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
    run {
        val claim = this.valueMap[claimId]!!
        for (subclaim in claim.subclaims) this.deleteClaimImpl(subclaim)
    }

    this.valueMap.remove(claimId)
    this.layerMap.remove(SortMapPriorityQueueKey(0, claimId))
}

fun ClaimStorage.deleteClaim(player: ServerPlayerEntity, claimId: UUID): Boolean {
    run {
        val claim = this.valueMap[claimId]!!
        if (player.uuid != claim.ownerId) return false

        val parentId = claim.parentClaimId
        if (parentId != null) {
            val parentClaim = this.valueMap[parentId] ?: throw NoSuchElementException("This error shouldn't happen LOL!")
            parentClaim.subclaims.remove(claimId)
        }
    }

    this.deleteClaimImpl(claimId)
}