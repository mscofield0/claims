@file:JvmName("BlockInteract")

package org.scofield.claims.event_handlers

import net.minecraft.block.BlockState
import net.minecraft.block.LecternBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.BookItem
import net.minecraft.item.WritableBookItem
import net.minecraft.item.WrittenBookItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World
import org.scofield.claims.claim.Claim
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPermissionCheckType
import org.scofield.claims.ext.toPoint
import org.scofield.claims.ext.toServerWorld
import org.scofield.claims.permission.BlockInteractEventPermissionMap
import org.scofield.claims.permission.ClaimPermission
import org.scofield.claims.types.*

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

fun handleInteractiveBlocks(blockState: BlockState, player: ServerPlayerEntity, claim: Claim): Boolean {
    return when(blockState.block) {
        is Storage -> claim.hasPermission(player.uuid, ClaimPermission.OPEN_STORAGE)
        is Door -> claim.hasPermission(player.uuid, ClaimPermission.OPEN_DOOR)
        is Workbench -> claim.hasPermission(player.uuid, ClaimPermission.OPEN_WORKBENCH)
        is Food -> claim.hasPermission(player.uuid, ClaimPermission.EAT_FOOD)
        is Redstone -> claim.hasPermission(player.uuid, ClaimPermission.INTERACT_WITH_REDSTONE)
        is LecternBlock -> {
            // Check if there is a book on the Lectern
            val hasBook = blockState.get(LecternBlock.HAS_BOOK)
            return if (hasBook) {
                claim.hasPermission(player.uuid, ClaimPermission.OPEN_LECTERN)
            } else {
                val mainHandItem = player.mainHandStack.item
                val offHandItem = player.offHandStack.item

                when(mainHandItem) {
                    is BookItem, is WritableBookItem, is WrittenBookItem
                        -> return claim.hasPermission(player.uuid, ClaimPermission.PLACE_LECTERN_BOOK)
                }

                return when(offHandItem) {
                    is BookItem, is WritableBookItem, is WrittenBookItem -> {
                        return if (mainHandItem is BlockItem) true
                        else claim.hasPermission(player.uuid, ClaimPermission.PLACE_LECTERN_BOOK)
                    }
                    else -> true
                }
            }
        }
        else -> claim.hasPermission(player.uuid, ClaimPermission.INTERACT_WITH_MISC_BLOCKS)
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
    val blockState = world.getBlockState(hitResult.blockPos)
    val playerIsCrouched = player.shouldCancelInteraction()

    return if (playerIsCrouched) {
        if (itemStack.isEmpty) {
            if (handleInteractiveBlocks(blockState, player, claim)) ActionResult.PASS
            else ActionResult.FAIL
        } else {
            if (claim.hasPermission(player.uuid, ClaimPermission.PLACE_BLOCKS)) ActionResult.PASS
            else ActionResult.FAIL
        }
    } else {
        if (handleInteractiveBlocks(blockState, player, claim)) ActionResult.PASS
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