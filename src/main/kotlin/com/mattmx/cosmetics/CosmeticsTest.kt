package com.mattmx.cosmetics

import com.github.retrooper.packetevents.PacketEvents
import com.mattmx.ktgui.GuiManager
import com.mattmx.ktgui.commands.rawCommand
import me.tofaa.entitylib.APIConfig
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform
import org.bukkit.plugin.java.JavaPlugin

class CosmeticsTest : JavaPlugin() {
    val movementSync = MovementSync()

    override fun onEnable() {
        instance = this
        GuiManager.init(this)

        val pa = PacketEvents.getAPI()

        val platform = SpigotEntityLibPlatform(this)
        val settings = APIConfig(pa)
            .debugMode()
            .tickTickables()
            .trackPlatformEntities()
            .usePlatformLogger()

        EntityLib.init(platform, settings)

        pa.eventManager.registerListeners(movementSync)

        rawCommand("test") {
            playerOnly = true
            suggestSubCommands = true

            subCommand(rawCommand("tophat") {
                runs {
                    TestTopHat().createFor(player)
                }
            })

            subCommand(rawCommand("backpack") {
                runs {
                    TestBackpack().createFor(player)
                }
            })

        }.register(false)
    }

    companion object {
        private lateinit var instance: CosmeticsTest
        fun get() = instance
    }

}