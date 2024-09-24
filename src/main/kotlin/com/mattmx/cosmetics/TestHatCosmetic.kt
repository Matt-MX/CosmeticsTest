package com.mattmx.cosmetics

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.util.Vector3f
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class TestHatCosmetic(
    private val player: Player
) {
    val ridingEntity = WrapperEntity(EntityTypes.ITEM_DISPLAY)
        .apply {
            consumeEntityMeta(ItemDisplayMeta::class.java) { meta ->
                meta.item = SpigotConversionUtil.fromBukkitItemStack(ItemStack(Material.STONE))
                meta.scale = Vector3f(1f, 0.25f, 1f)
                meta.translation = Vector3f(0.5f, 1f, 0.5f)
                meta.positionRotationInterpolationDuration = 1
            }
            spawn(SpigotConversionUtil.fromBukkitLocation(player.location.add(0.0, 2.0, 0.0)))
        }
    val repeatingTask = Bukkit.getScheduler()
        .runTaskTimerAsynchronously(CosmeticsTest.get(), {->
            ridingEntity.rotateHead(player.yaw, player.pitch)
            ridingEntity.refresh()
        }, 1L, 1L)

    fun getPassengerPacket(): WrapperPlayServerSetPassengers {
        return WrapperPlayServerSetPassengers(this.player.entityId, intArrayOf(this.ridingEntity.entityId))
    }

    fun sendPassengerPacketTo(sendTo: Player) {
        PacketEvents.getAPI()
            .playerManager
            .sendPacket(sendTo, getPassengerPacket())
    }
    
}