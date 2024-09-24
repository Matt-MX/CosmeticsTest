package com.mattmx.cosmetics

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.util.Vector3f
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TestTopHat {
    val builder = CosmeticBuilder()
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.COAL_BLOCK))
            scale = Vector3f(0.55f, 0.2f, 0.55f)
            positionRotationInterpolationDuration = 1
        }
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.COAL_BLOCK))
            scale = Vector3f(0.45f, 1f, 0.45f)
            translation = Vector3f(0f, 0.5f, 0f)
            positionRotationInterpolationDuration = 1
        }

    fun createFor(player: Player) {
        val entities = builder.create(player)

        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(CosmeticsTest.get(), { ->
                entities.forEach { ridingEntity ->
                    ridingEntity.rotateHead(player.yaw, player.pitch)
                }
            }, 1L, 1L)
    }
}