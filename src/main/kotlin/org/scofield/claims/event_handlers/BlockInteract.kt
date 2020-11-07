@file:JvmName("BlockInteract")

package org.scofield.claims.event_handlers

import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World
import org.scofield.claims.claim.Claim
import org.scofield.claims.claim.hasPermission
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
            if (owner !is ServerPlayerEntity) return true

            val neededPermission = BlockInteractEventPermissionMap.PROJECTILE_PERMISSION_MAP[blockType] ?: return false

            return claim.hasPermission(owner.uuid, neededPermission)
        }
    }

    return true
}

fun permitBreakTurtleEgg(world: ServerWorld, blockPos: BlockPos, entity: Entity): Boolean {
    val claimStorage = world.getClaimStorage()
    val pos = blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    return when(entity) {
        is ServerPlayerEntity -> claim.hasPermission(entity.uuid, ClaimPermission.TRAMPLE_TURTLE_EGG)
        is ProjectileEntity -> {
            val owner = entity.owner
            if (owner is ServerPlayerEntity) {
                claim.hasPermission(owner.uuid, ClaimPermission.TRAMPLE_TURTLE_EGG)
            } else {
                false
            }
        }

        // Any other entity interacting with the block should fail, as there is no way to
        // detect if the entity has been manipulated by a player.
        else -> false
    }
}

fun handleInteractiveBlocks(blockEntity: BlockEntity?, player: ServerPlayerEntity, claim: Claim): Boolean {
    if (blockEntity == null) return false

    return when(blockEntity) {
        is LootableContainerBlockEntity -> claim.hasPermission(player.uuid, ClaimPermission.OPEN_STORAGE)
        is DoorBlock
    }
}

fun permitUseBlocks(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult): ActionResult {
    val player = player as ServerPlayerEntity
    val world = world as ServerWorld

    // [NOTE] - Perhaps add claiming sticks for QOL

    val claimStorage = world.getClaimStorage()
    val pos = hitResult.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return ActionResult.PASS

    val itemStack = player.getStackInHand(hand)
    val blockEntity = world.getBlockEntity(hitResult.blockPos)
    val playerIsCrouched = player.shouldCancelInteraction()

    return if (playerIsCrouched) {
        if (itemStack.isEmpty) {
            if (handleInteractiveBlocks(blockEntity, player, claim)) ActionResult.PASS
            else ActionResult.FAIL
        } else {
            if (claim.hasPermission(player.uuid, ClaimPermission.PLACE_BLOCKS)) ActionResult.PASS
            else ActionResult.FAIL
        }
    } else {
        if (handleInteractiveBlocks(blockEntity, player, claim)) ActionResult.PASS
        else ActionResult.FAIL
    }
}

fun permitBreakBlocks(world: World, player: PlayerEntity, blockPos: BlockPos, blockState: BlockState, blockEntity: BlockEntity): Boolean{
    val player = player as ServerPlayerEntity
    val world = world as ServerWorld
    val claimStorage = world.getClaimStorage()
    val pos = blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    return claim.hasPermission(player.uuid, ClaimPermission.REMOVE_BLOCKS)
}