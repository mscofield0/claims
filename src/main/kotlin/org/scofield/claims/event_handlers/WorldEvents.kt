@file:JvmName("WorldEvents")

package org.scofield.claims.event_handlers

import net.minecraft.block.BlockState
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.explosion.Explosion
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPermissionCheckType
import org.scofield.claims.ext.toPoint
import org.scofield.claims.permission.ClaimPermission
import org.scofield.claims.permission.WorldEventPermissionMap

fun permitFireSpread(world: ServerWorld, pos: BlockPos): Boolean {
    val claimStorage = world.getClaimStorage()
    val claim = claimStorage.claimAtPos(pos.toPoint()) ?: return true

    return claim.hasPermission(ClaimPermission.FIRE_SPREAD)
}

fun permitFluidFlow(state: BlockState, world: ServerWorld, pos: BlockPos, direction: Direction): Boolean {
    // We don't care if the fluid is moving up or down
    if (direction == Direction.UP || direction == Direction.DOWN) return true

    val claimStorage = world.getClaimStorage()
    val to = claimStorage.claimAtPos(pos.offset(direction).toPoint())

    // The fluid is flowing into freeland
    if (to == null) return true
    // Allows any fluids to flow
    if (to.hasPermission(ClaimPermission.GENERIC_FLUID_FLOW)) return true

    // Specializes the fluid type permission
    val fluidType = state.fluidState.fluid.toPermissionCheckType()
    val neededPermission = WorldEventPermissionMap.FLUID_PERMISSION_MAP[fluidType] ?: return false

    return to.hasPermission(neededPermission)
}

fun cancelExplosion(world: ServerWorld, explosion: Explosion) {
    val claimStorage = world.getClaimStorage()
    explosion.affectedBlocks.removeIf { pos ->
        val claim = claimStorage.claimAtPos(pos.toPoint()) ?: return@removeIf false
        return@removeIf claim.hasPermission(ClaimPermission.EXPLOSIONS)
    }
}

fun permitPistonPushPull(state: BlockState, world: ServerWorld, pos: BlockPos, direction: Direction): Boolean {
    if (direction == Direction.UP || direction == Direction.DOWN) return true
    val claimStorage = world.getClaimStorage()
    val to = claimStorage.claimAtPos(pos.offset(direction).toPoint()) ?: return true

    return when(state.pistonBehavior) {
        PistonBehavior.DESTROY -> {
            to.hasPermission(ClaimPermission.INTERCLAIM_PISTON_DESTROY)
        }
        else -> to.hasPermission(ClaimPermission.INTERCLAIM_PISTON_PUSH_PULL)
    }
}

fun permitStartRaid(world: ServerWorld, player: ServerPlayerEntity): Boolean {
    val claimStorage = world.getClaimStorage()
    val pos = player.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    return claim.hasPermission(player.uuid, ClaimPermission.START_RAID)
}