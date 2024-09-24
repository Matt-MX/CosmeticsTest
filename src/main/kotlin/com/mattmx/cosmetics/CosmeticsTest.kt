package com.mattmx.cosmetics

import com.github.retrooper.packetevents.PacketEvents
import com.mattmx.ktgui.GuiManager
import com.mattmx.ktgui.commands.rawCommand
import me.tofaa.entitylib.APIConfig
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform
import org.bukkit.plugin.java.JavaPlugin

class CosmeticsTest : JavaPlugin() {

    override fun onEnable() {
        instance = this
        GuiManager.init(this)

        val platform = SpigotEntityLibPlatform(this)
        val settings = APIConfig(PacketEvents.getAPI())
            .debugMode()
            .tickTickables()
            .trackPlatformEntities()
            .usePlatformLogger()

        EntityLib.init(platform, settings)

        rawCommand("test") {
            playerOnly = true

            runs {
                val cosmetic = TestHatCosmetic(player)

                cosmetic.ridingEntity.addViewer(player.uniqueId)
                cosmetic.sendPassengerPacketTo(player)
            }
        }.register(false)
    }

    companion object {
        private lateinit var instance: CosmeticsTest
        fun get() = instance
    }

}