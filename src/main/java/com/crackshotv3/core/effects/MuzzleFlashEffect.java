package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * 銃口フラッシュ（短時間で明るい閃光）
 */
public class MuzzleFlashEffect implements Effect {

    private final int particleCount;
    private final double spread;

    public MuzzleFlashEffect(int particleCount, double spread) {
        this.particleCount = particleCount;
        this.spread = spread;
    }

    @Override
    public void play(Player player) {
        play(player.getLocation().add(0, 1.5, 0)); // 銃口高さを仮設定
    }

    @Override
    public void play(Location location) {
        location.getWorld().spawnParticle(Particle.FLASH, location, particleCount, spread, spread, spread);
    }
}
