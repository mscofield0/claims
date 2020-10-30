package org.scofield.claims.ext

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

fun World.toServerWorld(): ServerWorld = this as ServerWorld