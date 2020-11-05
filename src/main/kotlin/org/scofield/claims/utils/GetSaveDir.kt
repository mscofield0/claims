package org.scofield.claims.utils

import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.WorldSavePath
import net.minecraft.world.dimension.DimensionType
import java.io.File

fun getSaveDir(server: MinecraftServer, world: ServerWorld): File =
    DimensionType.getSaveDirectory(world.registryKey, server.getSavePath(WorldSavePath.ROOT).toFile())