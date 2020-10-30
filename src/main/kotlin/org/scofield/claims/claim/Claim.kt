package org.scofield.claims.claim

import org.scofield.claims.permission.*

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import org.scofield.claims.claim.storage.ClaimStorage
import org.scofield.claims.claim.storage.getClaim
import org.scofield.claims.ext.toPoint
import org.scofield.claims.utils.*

import java.util.*

data class Claim(
    // Identifiers
    val ownerId: UUID,

    // Location and space
    var area: Rect,

    // Relation to other claims
    var parentClaimId: UUID? = null,
    val subclaims: MutableList<UUID> = mutableListOf(),

    // Permissions
    val defaultPermissions: ClaimPermissions = defaultClaimPermissions(),
    val groupPermissions: MutableMap<String, GroupClaimPermissions> = mutableMapOf(),

    // Tracking flags
    var dirty: Boolean = false,
    var removed: Boolean = false,
)

infix fun Claim.intersects(other: Claim) = this.area intersects other.area
infix fun Claim.contains(other: Claim) = this.area partOf other.area

fun Claim.hasPermission(playerId: UUID, perm: ClaimPermission): Boolean {
    // Checks if the player is the creator of the claim
    if (this.ownerId == playerId) return true

    // Checks if the player satisfies default permissions
    if (this.defaultPermissions.contains(perm)) return true

    // Checks if the player is in a group that satisfies the required permission
    for ((_, groupPerms) in this.groupPermissions) {
        val isInGroup = groupPerms.playersInGroup.find { id -> id == playerId } != null
        if (isInGroup) {
            val hasPerm = groupPerms.permissions.contains(perm)
            if (hasPerm) return true
        }
    }

    return false
}

fun Claim.canPlayerInteract(storage: ClaimStorage, player: ServerPlayerEntity, perm: ClaimPermission, pos: BlockPos): Boolean {
    val point = pos.toPoint()
    return this.canPlayerInteract(storage, player, perm, point)
}

fun Claim.canPlayerInteract(storage: ClaimStorage, player: ServerPlayerEntity, perm: ClaimPermission, point: Point): Boolean {
    // Check if the player is in a subclaim
    for (subclaimId in this.subclaims) {
        val subclaim = storage.getClaim(subclaimId)!!
        if (point inside subclaim) {
            subclaim.canPlayerInteract(storage, player, perm, point)
        }
    }

    return this.hasPermission(player.uuid, perm)
}