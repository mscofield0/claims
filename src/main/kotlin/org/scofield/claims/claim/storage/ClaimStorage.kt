package org.scofield.claims.claim.storage

import org.scofield.claims.claim.Claim
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
import java.util.*
import kotlin.NoSuchElementException
import com.google.gson.Gson
import org.scofield.claims.utils.Point
import java.io.FileWriter

data class ClaimStorage(
    private val layerMap: TreeSet<ClaimStoragePriorityQueueKey>,
    private val valueMap: HashMap<UUID, Claim>
) {
    /**
     * Writes the key-value pair into the storage.
     *
     * @param key The key.
     * @param value A pair containing the layer depth and the [Claim].
     */
    operator fun set(key: UUID, value: Pair<Int, Claim>) {
        this.layerMap.add(ClaimStoragePriorityQueueKey(value.first, key))
        this.valueMap[key] = value.second
    }

    /**
     * Saves the [ClaimStorage] into the world's save folder.
     *
     * @param server The Minecraft server.
     * @param world The current [ServerWorld].
     */
    fun save(server: MinecraftServer, world: ServerWorld) {
        val saveDir = DimensionType.getSaveDirectory(world.registryKey, server.getSavePath(WorldSavePath.ROOT).toFile())
        val file = File(saveDir, "/data/claims/claims.json")

        file.createNewFile()

        Gson().toJson(this, FileWriter(file))
    }

    /**
     * Gets the claim from the [valueMap].
     *
     * @param claimId The claim's ID.
     *
     * @return The claim or null if it doesn't exist.
     */
    fun getClaim(claimId: UUID): Claim? {
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
    fun generateUuid(): UUID {
        val uuid = UUID.randomUUID()
        if (this.valueMap.containsKey(uuid)) return this.generateUuid()
        return uuid
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
    fun getCollidingClaim(playerId: UUID, claim: Claim): CollidingClaimReturnType {
        for ((layer, key) in this.layerMap) {
            val value = this.valueMap[key]!!
            if (claim.area intersects value.area) {
                return if (value.hasPermission(playerId, ClaimPermission.CREATE_CLAIM)) {
                    CollidingClaimReturnType(true, Pair(layer, key))
                } else {
                    CollidingClaimReturnType(true)
                }
            }
        }
        return CollidingClaimReturnType()
    }

    /**
     * Creates a new claim.
     *  - Creates a subclaim if the player is trying to claim inside an already existing claim
     *
     * @param player The player creating the claim.
     * @param area The area the claim should be covering.
     *
     * @throws StackOverflowError If the [generateUuid] function recurses for long enough.
     *
     * @return A boolean indicating if the creation was successful
     */
    fun createClaim(player: ServerPlayerEntity, area: Rect): Boolean {
        val uuid = this.generateUuid()
        val claim = Claim(player.uuid, area)
        val (collidingClaimExists, entryPair) = this.getCollidingClaim(player.uuid, claim)

        // Create a subclaim
        if (collidingClaimExists) {
            if (entryPair == null) {
                // Add a response to the player
                return false
            }

            val (layer, parentClaimId) = entryPair

            claim.parentClaimId = parentClaimId

            val parentClaim = this.valueMap[parentClaimId]!!
            parentClaim.subclaims.add(uuid)

            this[uuid] = Pair(layer, claim)

            return true
        }

        // Create a normal claim
        this[uuid] = Pair(0, claim)

        return true
    }

    /**
     * Internal implementation function for [deleteClaim]
     */
    private fun deleteClaimImpl(claimId: UUID) {
        // Delete this claim's subclaims recursively
        run {
            val claim = this.valueMap[claimId]!!
            for (subclaim in claim.subclaims) this.deleteClaimImpl(subclaim)
        }

        this.valueMap.remove(claimId)
        this.layerMap.remove(ClaimStoragePriorityQueueKey(0, claimId))
    }

    /**
     * Deletes the claim if the player has sufficient permissions.
     *
     * @param player The player deleting the claim.
     * @param claimId The claim's ID.
     *
     * @return A boolean indicating if the claim was deleted or not.
     */
    fun deleteClaim(player: ServerPlayerEntity, claimId: UUID): Boolean {
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

    /**
     * Finds a claim at the given position
     *
     * @param point 2D point indicating the flat position in the world
     *
     * @return The found claim or null if no claim was found.
     */
    fun claimAtPos(point: Point): Claim? {
        for ((_, key) in this.layerMap) {
            val claim = this.valueMap[key]!!
            if (point inside claim.area) return claim
        }

        return null
    }
}

// sucks not to have Rust enums
data class CollidingClaimReturnType (
    val claimExists: Boolean = false,
    val pair: Pair<Int, UUID>? = null
)