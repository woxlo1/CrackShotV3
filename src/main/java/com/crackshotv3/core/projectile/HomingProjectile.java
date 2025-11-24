package com.crackshotv3.core.projectile;

import com.crackshotv3.core.attachments.AttachmentStats;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * ホーミング弾（ProjectileBase + アタッチメント統合対応）
 */
public class HomingProjectile extends ProjectileBase {

    private final double baseSpeed = 3.0;   // 基本速度
    private double speed;                   // アタッチメント補正済み速度
    private double hitRadius = 0.3;         // 当たり判定半径
    private Player target;                  // 追尾対象

    public HomingProjectile(String id, Player shooter, Vector position, Vector direction, Player target, Weapon weapon) {
        super(id, shooter, position, direction);
        this.target = target;

        // 武器とアタッチメント補正を統合
        setWeapon(weapon);

        // 精度倍率で速度を補正
        AttachmentStats stats = attachmentStats != null ? attachmentStats : AttachmentStats.DEFAULT;
        speed = baseSpeed * stats.getAccuracyMultiplier();

        LoggerUtil.debug("[HomingProjectile] Created. BaseSpeed=" + baseSpeed + ", AdjustedSpeed=" + speed);
    }

    @Override
    public void onTick() {
        if (isExpired()) return;

        // ===============================
        // ターゲット追尾
        // ===============================
        if (target != null && !target.isDead()) {
            Vector toTarget = target.getLocation().toVector().subtract(getPosition()).normalize();
            setDirection(toTarget);
        }

        // ===============================
        // 次の位置計算
        // ===============================
        Vector nextPos = getPosition().clone().add(getDirection().multiply(speed));
        Location nextLoc = new Location(getShooter().getWorld(), nextPos.getX(), nextPos.getY(), nextPos.getZ());

        // ===============================
        // ブロック判定
        // ===============================
        Block block = nextLoc.getBlock();
        if (!block.isPassable()) {
            LoggerUtil.debug("[HomingProjectile] Hit block: " + block.getType() + " - " + getId());
            onHitBlock(block);
            expire();
            return;
        }

        // ===============================
        // エンティティ判定
        // ===============================
        List<LivingEntity> nearby = getShooter().getWorld()
                .getNearbyEntities(nextLoc, hitRadius, hitRadius, hitRadius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != getShooter())
                .map(e -> (LivingEntity) e)
                .toList();

        if (!nearby.isEmpty()) {
            nearby.forEach(this::onHitEntity);
            LoggerUtil.debug("[HomingProjectile] Hit entities: " + nearby.size() + " - " + getId());
            expire();
            return;
        }

        // ===============================
        // 位置更新
        // ===============================
        setPosition(nextPos);
        LoggerUtil.debug("[HomingProjectile] Position updated: " + getPosition());
    }

    protected void onHitBlock(Block block) {
        // デフォルトでは何もしない
    }

    protected void onHitEntity(LivingEntity entity) {
        entity.damage(getModifiedDamage(), getShooter()); // ProjectileBase の補正済ダメージを使用
    }

    // ===============================
    // Getter / Setter
    // ===============================
    public Player getTarget() { return target; }
    public void setTarget(Player target) { this.target = target; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getHitRadius() { return hitRadius; }
    public void setHitRadius(double hitRadius) { this.hitRadius = hitRadius; }
}
