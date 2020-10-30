@file:JvmName("BlockInteract")

package org.scofield.claims.event_handlers

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun cancelEntityBlockCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity): Boolean {
    val block = state.block
    when {
        entity is ServerPlayerEntity -> {

        }
    }

    return true
}