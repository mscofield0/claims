@file:JvmName("BlockInteract")

package org.scofield.claims.event_handlers

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.world.World
import org.scofield.claims.claim.hasPermission
import org.scofield.claims.claim.storage.claimAtPos
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPermissionCheckType
import org.scofield.claims.ext.toPoint
import org.scofield.claims.ext.toServerWorld
import org.scofield.claims.permission.BlockInteractEventPermissionMap
import org.scofield.claims.permission.ClaimPermission

fun permitEntityBlockCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity): Boolean {
    val claimStorage = world.toServerWorld().getClaimStorage()
    val claim = claimStorage.claimAtPos(pos.toPoint()) ?: return true

    // Allows any block to be interacted with
    if (claim.hasPermission(ClaimPermission.GENERIC_BLOCK_INTERACTION)) return true

    val blockType = state.block.toPermissionCheckType()
    when (entity) {
        is ServerPlayerEntity -> {
            val neededPermission = BlockInteractEventPermissionMap.ENTITY_PERMISSION_MAP[blockType] ?: return false

            return claim.hasPermission(entity.uuid, neededPermission)
        }
        is ProjectileEntity -> {
            // If the projectile has no owner, it originated from an automated source
            // thus we need to cancel the event as it poses a security risk.
            val owner = entity.owner ?: return false

            val neededPermission = BlockInteractEventPermissionMap.PROJECTILE_PERMISSION_MAP[blockType] ?: return false

            return claim.hasPermission(owner.uuid, neededPermission)
        }
    }

    return true
}