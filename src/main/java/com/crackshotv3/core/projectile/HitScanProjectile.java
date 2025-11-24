package com.crackshotv3.core.projectile;

import com.crackshotv3.core.util.LoggerUtil;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * 即時ヒット弾（Hitscan） + 武器・アタッチメント補正統合版
 */
public class HitScanProjectile extends ProjectileBase {

    private final double baseMaxDistance = 50.0; // 基本射程
    private double hitRadius = 0.3;              // 命中判定半径

    public HitScanProjectile(String id, Player shooter, Vector position, Vector direction, Weapon weapon) {
        super(id, shooter, position, direction);
        setWeapon(weapon); // 武器とアタッチメント補正を統合
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        LoggerUtil.debug("[HitScanProjectile] Processing hitscan: " + getId());

        Vector step = getDirection().clone().normalize().multiply(0.5);
        Vector currentPos = getPosition();
        double maxDistance = baseMaxDistance * attachmentStats.getAccuracyMultiplier(); // 射程補正
        double traveled = 0;

        while (traveled < maxDistance) {
            currentPos.add(step);
            traveled += step.length();

            Location loc = new Location(getShooter().getWorld(), currentPos.getX(), currentPos.getY(), currentPos.getZ());

            // ブロック判定
            Block block = loc.getBlock();
            if (!block.isPassable()) {
                LoggerUtil.debug("[HitScanProjectile] Hit block: " + block.getType() + " - " + getId());
                onHitBlock(block);
                expire();
                return;
            }

            // エンティティ判定
            List<LivingEntity> nearby = getShooter().getWorld()
                    .getNearbyEntities(loc, hitRadius, hitRadius, hitRadius)
                    .stream()
                    .filter(e -> e instanceof LivingEntity && e != getShooter())
                    .map(e -> (LivingEntity) e)
                    .toList();

            if (!nearby.isEmpty()) {
                for (LivingEntity le : nearby) {
                    LoggerUtil.debug("[HitScanProjectile] Hit entity: " + le.getName() + " - " + getId());
                    onHitEntity(le);
                }
                expire();
                return;
            }
        }

        LoggerUtil.debug("[HitScanProjectile] No hit, expiring: " + getId());
        expire();
    }

    protected void onHitBlock(Block block) {
        // デフォルトでは何もしない
    }

    protected void onHitEntity(LivingEntity entity) {
        entity.damage(getModifiedDamage(), getShooter());
    }

    public double getHitRadius() {
        return hitRadius;
    }

    public void setHitRadius(double hitRadius) {
        this.hitRadius = hitRadius;
    }

    public double getMaxDistance() {
        return baseMaxDistance * attachmentStats.getAccuracyMultiplier();
    }
}
