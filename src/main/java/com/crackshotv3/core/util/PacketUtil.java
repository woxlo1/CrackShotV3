package com.crackshotv3.core.util;

import org.bukkit.entity.Player;
import org.bukkit.Particle;

public class PacketUtil {

    public static void sendParticle(Player player, Particle particle, double x, double y, double z) {
        player.spawnParticle(
                particle,
                player.getLocation().getX() + x,
                player.getLocation().getY() + y,
                player.getLocation().getZ() + z,
                1 // パーティクルの数
        );
    }

}
