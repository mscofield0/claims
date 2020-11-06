package org.scofield.claims

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import org.scofield.claims.event_handlers.permitUseBlocks

// For support join https://discord.gg/v6v4pMv

object Main : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        UseBlockCallback.EVENT.register(::permitUseBlocks)
    }
}

