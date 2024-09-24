package com.mattmx.cosmetics

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.type.EntityType
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player

class CosmeticBuilder {
    val parts = mutableListOf<() -> WrapperEntity>()

    inline fun <reified T : EntityMeta> part(type: EntityType, noinline consumer: T.() -> Unit) = apply {
        parts += {
            val entity = WrapperEntity(type)
            entity.consumeEntityMeta(T::class.java, consumer)
            entity
        }
    }

    fun create(player: Player) : List<WrapperEntity> {
        val entities = parts.map { it().apply {
            spawn(SpigotConversionUtil.fromBukkitLocation(player.location))
            addViewer(player.uniqueId) }
        }

        val packet = TestHatCosmetic.getPassengerPacket(player, *entities.map { it.entityId }.toTypedArray().toIntArray())

        PacketEvents.getAPI()
            .playerManager
            .sendPacket(player, packet)

        return entities
    }
}