@file:JvmName("PlayerClaimDataFactory")

package org.scofield.claims.claim.storage

import com.google.gson.Gson
import net.minecraft.server.MinecraftServer
import net.minecraft.util.WorldSavePath
import org.scofield.claims.ext.fromJson
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

class PlayerClaimDataFactory {
    companion object {
        fun readFromSave(server: MinecraftServer, playerId: UUID): PlayerClaimData {
            val saveDir = server.getSavePath(WorldSavePath.PLAYERDATA).toFile()
            val file = File(saveDir, "/claims/player_claim_data/$playerId.json")
            if (!file.exists()) throw IOException("The player save file for Scofield.Claims mod is missing.")

            return Gson().fromJson<PlayerClaimData>(FileReader(file))
        }
    }
}
