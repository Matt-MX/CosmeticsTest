package com.mattmx.cosmetics

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation
import net.minecraft.util.Mth.wrapDegrees
import org.bukkit.entity.Player
import kotlin.math.abs
import kotlin.math.atan2

class MovementSync : PacketListenerAbstract() {
    private var lastReceivedRawYaw = 0f
    var bodyYaw = 0f
        private set

    override fun onPacketReceive(event: PacketReceiveEvent) {
        if (event.packetType != PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) return

        tickMovement(event.player as Player, WrapperPlayClientPlayerPositionAndRotation(event))
    }

    fun tickMovement(player: Player, flying: WrapperPlayClientPlayerPositionAndRotation) {
        val to = flying.location// Where we're moving to
        val from = player.location // Where we moved from

        // Our yaw is actually the internal yaw of the player, rather than the wrapped yaw
        var yaw = if (flying.hasRotationChanged()) to.yaw else lastReceivedRawYaw
        var i = to.x - from.x;
        var d = to.z - from.z;
        var f = (i * i + d * d).toFloat();
        var g = this.bodyYaw;
        if (f > 0.0025000002F) {
            // Using internal Mojang math utils here
            val l = atan2(d, i).toFloat() * 57.295776F - 90.0F;
            val m = abs(wrapDegrees(yaw) - l);
            g = if (95.0F < m && m < 265.0F) {
                l - 180.0F;
            } else {
                l
            }
        }

        this.turnBody(g, yaw);
    }

    fun turnBody(bodyRotation: Float, yaw: Float) {
        val f = wrapDegrees(bodyRotation - bodyYaw);
        bodyYaw += f * 0.3F;
        var g = wrapDegrees(yaw - bodyYaw);
        if (g < -75.0F) {
            g = -75.0F;
        }

        if (g >= 75.0F) {
            g = 75.0F;
        }

        this.bodyYaw = yaw - g;
        if (g * g > 2500.0F) {
            this.bodyYaw += g * 0.2F;
        }
    }
}