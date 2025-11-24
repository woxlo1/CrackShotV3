package com.crackshotv3.core.projectile;

import com.crackshotv3.core.attachments.AttachmentStats;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * 貫通弾（PiercingProjectile + ProjectileBase + アタッチメント統合対応）
 */
public class PiercingProjectile extends ProjectileBase {

    private double baseSpeed = 3.0;      // 基本速度
    private double speed;                // 補正済み速度
    private double hitRadius = 0.5;      // 当たり判定半径
    private double maxDistance = 100;    // 最大飛距離
    private double traveled = 0;         // 現在の飛行距離
    private final List<LivingEntity> hitEntities = new ArrayList<>();

    public PiercingProjectile(String id, Player shooter, Vector position, Vector direction, Weapon weapon) {
        super(id, shooter, position, direction);

        // 武器とアタッチメント統合
        setWeapon(weapon);

        // アタッチメント精度補正を速度に反映
        AttachmentStats stats = attachmentStats != null ? attachmentStats : AttachmentStats.DEFAULT;
        speed = baseSpeed * stats.getAccuracyMultiplier();

        LoggerUtil.debug("[PiercingProjectile] Created. BaseSpeed=" + baseSpeed + ", AdjustedSpeed=" + speed);
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        // 次の位置計算
        Vector nextPos = getPosition().clone().add(getDirection().clone().multiply(speed));
        traveled += speed;

        // 最大飛距離チェック
        if (traveled > maxDistance) {
            LoggerUtil.debug("[PiercingProjectile] Max distance reached, expiring: " + getId());
            expire();
            return;
        }

        Location nextLoc = new Location(getShooter().getWorld(), nextPos.getX(), nextPos.getY(), nextPos.getZ());

        // 周囲のエンティティにヒット判定
        List<LivingEntity> nearby = getShooter().getWorld()
                .getNearbyEntities(nextLoc, hitRadius, hitRadius, hitRadius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        for (LivingEntity le : nearby) {
            if (!hitEntities.contains(le)) {
                hitEntity(le);
                hitEntities.add(le);
                LoggerUtil.debug("[PiercingProjectile] Hit entity: " + le.getName() + " - " + getId());
            }
        }

        // 位置更新
        setPosition(nextPos);
        LoggerUtil.debug("[PiercingProjectile] Position updated: " + getPosition());
    }

    /**
     * エンティティにヒット
     */
    private void hitEntity(LivingEntity entity) {
        entity.damage(getModifiedDamage(), getShooter());
    }

    // ===============================
    // Getter / Setter
    // ===============================
    public double getHitRadius() { return hitRadius; }
    public void setHitRadius(double hitRadius) { this.hitRadius = hitRadius; }

    public double getMaxDistance() { return maxDistance; }
    public void setMaxDistance(double maxDistance) { this.maxDistance = maxDistance; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
