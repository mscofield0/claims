package org.scofield.claims.event_handlers

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockInteract {
    fun cancelEntityBlockCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity): Boolean {
        when {
            entity is ServerPlayerEntity -> {
                
            }
        }
    }
}