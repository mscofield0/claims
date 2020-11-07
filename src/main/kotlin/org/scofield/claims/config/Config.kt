package org.scofield.claims.config

import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import net.minecraft.server.MinecraftServer
import net.minecraft.util.WorldSavePath
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import org.scofield.claims.ext.fromJson
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

data class Config (
    var maxClaimLimit: Int,
    var maxClaimSize: Int
) {
    fun save() {
        val saveDir = FabricLoader.getInstance().configDir.toFile()
        val saveFile = File(saveDir, "/claims/config.json")

        saveFile.createNewFile()

        Gson().toJson(this, FileWriter(saveFile))
    }

    companion object {
        fun exists(): Boolean {
            val saveDir = FabricLoader.getInstance().configDir.toFile()
            val saveFile = File(saveDir, "/claims/config.json")

            return saveFile.exists()
        }

        fun default(): Config {
            return Config(
                maxClaimLimit = 3,
                maxClaimSize = 16 * 16
            )
        }

        fun createDefaultConfigFile() {
            val defaultConfig = Config.default()

            defaultConfig.save()
        }
    }
}

private fun loadConfig(): Config {
    if (!Config.exists()) Config.createDefaultConfigFile()

    val saveDir = FabricLoader.getInstance().configDir.toFile()
    val saveFile = File(saveDir, "/claims/config.json")

    return Gson().fromJson<Config>(FileReader(saveFile))
}

val config_di = DI {
    bind<Config>() with singleton { loadConfig() }
}
