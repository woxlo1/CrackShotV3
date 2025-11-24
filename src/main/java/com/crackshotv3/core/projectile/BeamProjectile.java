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
 * レーザー弾（直線ヒットスキャン） + アタッチメント統合対応
 */
public class BeamProjectile extends ProjectileBase {

    private final double baseMaxDistance = 50.0; // 基本射程

    public BeamProjectile(String id, Player shooter, Vector position, Vector direction, Weapon weapon) {
        super(id, shooter, position, direction);
        setWeapon(weapon); // 武器とアタッチメント補正を統合
        LoggerUtil.debug("[BeamProjectile] Created. BaseMaxDistance=" + baseMaxDistance);
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        // アタッチメント精度補正を射程に反映
        double maxDistance = baseMaxDistance * attachmentStats.getAccuracyMultiplier();

        Vector step = getDirection().clone().normalize().multiply(0.5);
        Vector currentPos = getPosition().clone();
        double traveled = 0;

        while (traveled < maxDistance) {
            currentPos.add(step);
            traveled += step.length();

            Location loc = new Location(getShooter().getWorld(), currentPos.getX(), currentPos.getY(), currentPos.getZ());
            Block block = loc.getBlock();

            // ブロック判定
            if (!block.isPassable() && block.getType() != Material.AIR) {
                LoggerUtil.debug("[BeamProjectile] Hit block: " + block.getType() + " - " + getId());
                expire();
                return;
            }

            // エンティティ判定
            List<LivingEntity> nearby = getShooter().getWorld()
                    .getNearbyEntities(loc, 0.5, 0.5, 0.5)
                    .stream()
                    .filter(e -> e instanceof LivingEntity && e != getShooter())
                    .map(e -> (LivingEntity) e)
                    .toList();

            if (!nearby.isEmpty()) {
                nearby.forEach(le -> le.damage(getModifiedDamage(), getShooter()));
                LoggerUtil.debug("[BeamProjectile] Hit entities: " + nearby.size() + " - " + getId());
                expire();
                return;
            }
        }

        LoggerUtil.debug("[BeamProjectile] Beam expired without hitting anything - " + getId());
        expire();
    }

    public double getMaxDistance() {
        return baseMaxDistance * attachmentStats.getAccuracyMultiplier();
    }

    public double getDamage() {
        return getModifiedDamage();
    }
}
