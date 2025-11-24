package com.crackshotv3.core.projectile;

import com.crackshotv3.core.attachments.AttachmentStats;
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
 * 跳弾弾丸（RicochetProjectile + ProjectileBase + アタッチメント統合対応）
 */
public class RicochetProjectile extends ProjectileBase {

    private final int baseMaxBounces = 3;   // 基本反射回数
    private int remainingBounces;
    private double hitRadius = 0.5;         // 当たり判定半径
    private double baseSpeed = 3.0;         // 基本速度
    private double speed;                   // 補正済み速度

    public RicochetProjectile(String id, Player shooter, Vector position, Vector direction, Weapon weapon) {
        super(id, shooter, position, direction);

        // 武器とアタッチメント統合
        setWeapon(weapon);

        // アタッチメント精度補正で速度を補正
        AttachmentStats stats = attachmentStats != null ? attachmentStats : AttachmentStats.DEFAULT;
        speed = baseSpeed * stats.getAccuracyMultiplier();
        remainingBounces = baseMaxBounces;

        LoggerUtil.debug("[RicochetProjectile] Created. BaseSpeed=" + baseSpeed + ", AdjustedSpeed=" + speed + ", MaxBounces=" + remainingBounces);
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        // 次フレームの位置計算
        Vector nextPos = getPosition().clone().add(getDirection().clone().multiply(speed));
        Location nextLoc = new Location(getShooter().getWorld(), nextPos.getX(), nextPos.getY(), nextPos.getZ());
        Block block = nextLoc.getBlock();

        // ブロック衝突判定
        if (!block.isPassable() && block.getType() != Material.AIR) {
            if (remainingBounces > 0) {
                Vector normal = calculateNormal(block, getPosition());
                reflect(normal);
                remainingBounces--;
                LoggerUtil.debug("[RicochetProjectile] Bounced on block " + block.getType() + ", remaining bounces: " + remainingBounces);
            } else {
                LoggerUtil.debug("[RicochetProjectile] Hit solid block, expiring: " + getId());
                expire();
                return;
            }
        }

        // エンティティ判定
        List<LivingEntity> nearby = getShooter().getWorld()
                .getNearbyEntities(nextLoc, hitRadius, hitRadius, hitRadius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        for (LivingEntity le : nearby) {
            hitEntity(le);
            LoggerUtil.debug("[RicochetProjectile] Hit entity: " + le.getName() + " - " + getId());
            expire();
            return;
        }

        // 位置更新
        setPosition(nextPos);
        LoggerUtil.debug("[RicochetProjectile] Position updated: " + getPosition());
    }

    /**
     * 衝突面の法線ベクトルを計算（単純近似）
     */
    private Vector calculateNormal(Block block, Vector projectilePos) {
        Vector blockCenter = block.getLocation().toVector().add(new Vector(0.5, 0.5, 0.5));
        Vector toProjectile = projectilePos.clone().subtract(blockCenter);

        double absX = Math.abs(toProjectile.getX());
        double absY = Math.abs(toProjectile.getY());
        double absZ = Math.abs(toProjectile.getZ());

        if (absX >= absY && absX >= absZ) return new Vector(Math.signum(toProjectile.getX()), 0, 0);
        if (absY >= absX && absY >= absZ) return new Vector(0, Math.signum(toProjectile.getY()), 0);
        return new Vector(0, 0, Math.signum(toProjectile.getZ()));
    }

    /**
     * 法線ベクトルに対して反射
     */
    private void reflect(Vector normal) {
        // V' = V - 2*(V·N)*N
        double dot = getDirection().dot(normal);
        Vector reflected = getDirection().clone().subtract(normal.clone().multiply(2 * dot));
        setDirection(reflected);
    }

    /**
     * エンティティにヒット
     */
    private void hitEntity(LivingEntity entity) {
        entity.damage(getModifiedDamage(), getShooter()); // ProjectileBaseの補正済ダメージを使用
    }

    // ===============================
    // Getter / Setter
    // ===============================
    public int getRemainingBounces() { return remainingBounces; }
    public void setRemainingBounces(int remainingBounces) { this.remainingBounces = remainingBounces; }

    public double getHitRadius() { return hitRadius; }
    public void setHitRadius(double hitRadius) { this.hitRadius = hitRadius; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
