@file:JvmName("ItemInteract")

package org.scofield.claims.event_handlers

import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import org.scofield.claims.claim.hasPermission
import org.scofield.claims.claim.storage.claimAtPos
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPoint
import org.scofield.claims.ext.toServerWorld
import org.scofield.claims.permission.ClaimPermission

fun permitUseBlockItem(context: ItemUsageContext): ActionResult {
    if (context.stack.isEmpty) return ActionResult.PASS

    val claimStorage = context.world.toServerWorld().getClaimStorage()
    val pos = context.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return ActionResult.PASS

    val player = context.player as ServerPlayerEntity? ?: return ActionResult.PASS

    if (claim.hasPermission(player.uuid, ClaimPermission.PLACE_BLOCKS)) return ActionResult.SUCCESS

    return ActionResult.FAIL
}