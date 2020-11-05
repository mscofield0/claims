@file:JvmName("ItemInteract")

package org.scofield.claims.event_handlers

import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import org.scofield.claims.claim.hasPermission
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPoint
import org.scofield.claims.ext.toServerWorld
import org.scofield.claims.permission.ClaimPermission

fun permitUseBlockItem(context: ItemUsageContext): Boolean {
    if (context.stack.isEmpty) return false

    val claimStorage = context.world.toServerWorld().getClaimStorage()
    val pos = context.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    val player = context.player as ServerPlayerEntity? ?: return false

    return claim.hasPermission(player.uuid, ClaimPermission.PLACE_BLOCKS)
}