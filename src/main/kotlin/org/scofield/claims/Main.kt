package org.scofield.claims

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.*
import net.minecraft.server.MinecraftServer
import org.kodein.di.instance
import org.scofield.claims.command.CommandSet
import org.scofield.claims.config.Config
import org.scofield.claims.config.config_di
import org.scofield.claims.event_handlers.permitBreakBlocks
import org.scofield.claims.event_handlers.permitUseBlocks

fun onServerExit(server: MinecraftServer) {
    val config: Config by config_di.instance()
    config.save()
}

@Suppress("unused")
object Main : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        PlayerBlockBreakEvents.BEFORE.register(::permitBreakBlocks)
        UseBlockCallback.EVENT.register(::permitUseBlocks)
        ServerLifecycleEvents.SERVER_STOPPING.register(::onServerExit)

        CommandRegistrationCallback.EVENT.register(CommandSet.Companion::register)
    }
}

