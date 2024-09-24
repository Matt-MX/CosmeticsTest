package com.mattmx.cosmetics

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.util.Vector3f
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TestBackpack {
    val builder = CosmeticBuilder()
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.COAL_BLOCK))
            scale = Vector3f(0.35f, 0.60f, 0.2f)
            translation = Vector3f(0f, -0.75f, -0.2f)
            positionRotationInterpolationDuration = 1
        }
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.COAL_BLOCK))
            scale = Vector3f(0.41f, 0.35f, 0.25f)
            translation = Vector3f(0f, -0.85f, -0.2f)
            positionRotationInterpolationDuration = 1
        }
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.GOLD_NUGGET))
            scale = Vector3f(0.2f, 0.2f, 0.2f)
            translation = Vector3f(0f, -0.55f, -0.305f)
            positionRotationInterpolationDuration = 1
        }
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.BROWN_WOOL))
            scale = Vector3f(0.1f, 0.78f, 0.4f)
            translation = Vector3f(0.1f, -0.77f, -0f)
            positionRotationInterpolationDuration = 1
        }
        .part<ItemDisplayMeta>(EntityTypes.ITEM_DISPLAY) {
            item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.BROWN_WOOL))
            scale = Vector3f(0.1f, 0.78f, 0.4f)
            translation = Vector3f(-0.1f, -0.77f, -0f)
            positionRotationInterpolationDuration = 1
        }

    fun createFor(player: Player) {
        val entities = builder.create(player)

        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(CosmeticsTest.get(), { ->
                entities.forEach { ridingEntity ->
                    ridingEntity.rotateHead(CosmeticsTest.get().movementSync.bodyYaw, 0f)
                }
            }, 1L, 1L)
    }
}