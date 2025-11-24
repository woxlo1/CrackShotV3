package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * 弾着弾時の効果
 */
public class ImpactEffect implements Effect {

    private final Particle particle;
    private final int count;

    public ImpactEffect(Particle particle, int count) {
        this.particle = particle;
        this.count = count;
    }

    @Override
    public void play(Player player) {
        play(player.getLocation());
    }

    @Override
    public void play(Location location) {
        location.getWorld().spawnParticle(particle, location, count, 0.2, 0.2, 0.2);
    }
}
