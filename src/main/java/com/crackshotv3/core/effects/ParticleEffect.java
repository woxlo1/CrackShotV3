package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleEffect implements Effect {

    private final Particle particle;
    private final int count;
    private final double offsetX, offsetY, offsetZ;

    public ParticleEffect(Particle particle, int count, double offsetX, double offsetY, double offsetZ) {
        this.particle = particle;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public void play(Player player) {
        player.spawnParticle(particle, player.getLocation(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void play(Location location) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }
}
