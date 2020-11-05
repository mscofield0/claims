package org.scofield.claims.claim.storage

import com.google.gson.Gson
import net.minecraft.server.MinecraftServer
import net.minecraft.util.WorldSavePath
import java.io.File
import java.io.FileWriter
import java.util.*

data class PlayerClaimData (
    val id: UUID,
    var numOfClaims: Int,
) {
    /**
     * Saves the [PlayerClaimData] into the server's player save folder.
     *
     * @param server The Minecraft server.
     */
    fun save(server: MinecraftServer) {
        val saveDir = server.getSavePath(WorldSavePath.PLAYERDATA).toFile()
        val file = File(saveDir, "/claims/player_claim_data/$id.json")

        file.createNewFile()

        Gson().toJson(this, FileWriter(file))
    }

}