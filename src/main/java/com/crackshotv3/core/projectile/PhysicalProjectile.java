package com.crackshotv3.core.projectile;

import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * 物理弾（ProjectileBase + アタッチメント統合対応）
 */
public class PhysicalProjectile extends ProjectileBase {

    private final double baseSpeed = 3.0;   // 基本速度
    private double speed;                   // アタッチメント補正済み速度
    private double hitRadius = 0.3;         // 当たり判定半径

    public PhysicalProjectile(String id, Player shooter, Vector position, Vector direction) {
        super(id, shooter, position, direction);
        speed = baseSpeed * attachmentStats.getAccuracyMultiplier();
        LoggerUtil.debug("[PhysicalProjectile] Created. BaseSpeed=" + baseSpeed + ", AdjustedSpeed=" + speed);
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        Vector nextPos = getPosition().clone().add(getDirection().multiply(speed));
        Location nextLoc = new Location(getShooter().getWorld(), nextPos.getX(), nextPos.getY(), nextPos.getZ());

        // ブロック判定
        Block block = nextLoc.getBlock();
        if (!block.isPassable()) {
            LoggerUtil.debug("[PhysicalProjectile] Hit block: " + block.getType() + " - " + getId());
            onHitBlock(block);
            expire();
            return;
        }

        // エンティティ判定
        List<LivingEntity> nearby = getShooter().getWorld().getNearbyEntities(nextLoc, hitRadius, hitRadius, hitRadius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        if (!nearby.isEmpty()) {
            nearby.forEach(le -> onHitEntity(le));
            LoggerUtil.debug("[PhysicalProjectile] Hit entities: " + nearby.size() + " - " + getId());
            expire();
            return;
        }

        // 位置更新
        setPosition(nextPos);
        LoggerUtil.debug("[PhysicalProjectile] Position updated: " + getPosition());
    }

    protected void onHitBlock(Block block) {
        // デフォルトでは何もしない
    }

    protected void onHitEntity(LivingEntity entity) {
        entity.damage(getModifiedDamage(), getShooter()); // アタッチメント補正済みダメージ
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getHitRadius() { return hitRadius; }
    public void setHitRadius(double hitRadius) { this.hitRadius = hitRadius; }
}
