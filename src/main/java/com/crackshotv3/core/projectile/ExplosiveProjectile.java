package com.crackshotv3.core.projectile;

import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * 爆風弾（PhysicalProjectile派生）
 * 武器・アタッチメント補正統合版
 */
public class ExplosiveProjectile extends PhysicalProjectile {

    private final double baseExplosionRadius = 3.0; // 基本爆風半径
    private boolean breakBlocks = false;

    public ExplosiveProjectile(String id, Player shooter, Vector position, Vector direction, Weapon weapon) {
        super(id, shooter, position, direction);
        setWeapon(weapon); // 武器とアタッチメント補正を統合
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        super.onTick();

        Location loc = new Location(getShooter().getWorld(), getPosition().getX(), getPosition().getY(), getPosition().getZ());
        Block block = loc.getBlock();

        // ブロック衝突判定
        if (!block.isPassable()) {
            LoggerUtil.debug("[ExplosiveProjectile] Hit block: " + block.getType() + " - " + getId());
            explode();
            expire();
            return;
        }

        // 周囲のエンティティ判定
        double radius = getExplosionRadius();
        List<LivingEntity> nearby = getShooter().getWorld()
                .getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        if (!nearby.isEmpty()) {
            LoggerUtil.debug("[ExplosiveProjectile] Hit entities: " + nearby.size() + " - " + getId());
            explode();
            expire();
        }
    }

    private void explode() {
        Location loc = new Location(getShooter().getWorld(), getPosition().getX(), getPosition().getY(), getPosition().getZ());

        double radius = getExplosionRadius();
        double damage = getModifiedDamage();

        // 爆風作成
        loc.getWorld().createExplosion(loc, (float) radius, breakBlocks, breakBlocks, null);

        // 周囲のエンティティにダメージ
        List<LivingEntity> nearby = getShooter().getWorld()
                .getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        for (LivingEntity le : nearby) {
            le.damage(damage, getShooter());
        }

        LoggerUtil.debug("[ExplosiveProjectile] Explosion at " + loc + ", entities hit: " + nearby.size());
    }

    /** アタッチメント補正を反映した爆風半径 */
    public double getExplosionRadius() {
        return baseExplosionRadius * attachmentStats.getAccuracyMultiplier();
    }

    public boolean isBreakBlocks() {
        return breakBlocks;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }
}
