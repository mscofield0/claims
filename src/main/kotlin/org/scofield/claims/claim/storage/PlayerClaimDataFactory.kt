@file:JvmName("PlayerClaimDataFactory")

package org.scofield.claims.claim.storage

import com.google.gson.Gson
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.WorldSavePath
import net.minecraft.world.dimension.DimensionType
import org.scofield.claims.ext.fromJson
import java.io.File
import java.io.FileReader
import java.io.IOException

fun readFromSave(server: MinecraftServer, world: ServerWorld): ClaimStorage {
    val saveDir = DimensionType.getSaveDirectory(world.registryKey, server.getSavePath(WorldSavePath.ROOT).toFile())
    val file = File(saveDir, "/data/claims/claims.json")
    if (!file.exists()) throw IOException("The save file for Scofield.Claims mod is missing.")

    return Gson().fromJson<ClaimStorage>(FileReader(file))
}