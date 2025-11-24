package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * 弾道トレイル
 */
public class TrailEffect implements Effect {

    private final Particle particle;
    private final int count;

    public TrailEffect(Particle particle, int count) {
        this.particle = particle;
        this.count = count;
    }

    @Override
    public void play(Player player) {
        play(player.getLocation());
    }

    @Override
    public void play(Location location) {
        location.getWorld().spawnParticle(particle, location, count, 0, 0, 0);
    }
}
