package com.crackshotv3.core.projectile;

import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentStats;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponStats;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

/**
 * すべての弾の基底クラス
 * 武器性能・アタッチメント補正を反映
 */
public abstract class ProjectileBase {

    protected final String id;         // 弾ID
    protected Player shooter;          // 射手（APIで変更可能）
    protected Vector position;         // 現在位置
    protected Vector direction;        // 進行方向
    private boolean expired = false;
    private final long spawnTick;

    // 武器性能
    protected Weapon weapon;
    protected WeaponStats baseStats;
    protected AttachmentStats attachmentStats;

    private double speed = 1.0; // デフォルト速度

    public ProjectileBase(String id, Player shooter, Vector position, Vector direction) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.shooter = shooter;
        this.position = position.clone();
        this.direction = direction.clone().normalize();
        this.spawnTick = System.currentTimeMillis();
        this.attachmentStats = AttachmentStats.DEFAULT;
    }

    // =========================
    //   武器関連
    // =========================

    /** 武器を関連付け、WeaponStats と AttachmentStats を計算 */
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        if (weapon != null) {
            this.baseStats = weapon.getStats();
            this.attachmentStats = computeAttachmentStats(weapon.getAttachments());
            this.speed = weapon.getStats().getProjectileSpeed(); // WeaponStats に速度がある前提
        } else {
            this.baseStats = null;
            this.attachmentStats = AttachmentStats.DEFAULT;
            this.speed = 1.0;
        }
    }

    /** アタッチメント補正の合算 */
    private AttachmentStats computeAttachmentStats(List<Attachment> attachments) {
        double recoil = 1.0;
        double damage = 1.0;
        double fireRate = 1.0;
        double accuracy = 1.0;

        for (Attachment a : attachments) {
            AttachmentStats s = a.getStats();
            recoil *= s.getRecoilMultiplier();
            damage *= s.getDamageMultiplier();
            fireRate *= s.getFireRateMultiplier();
            accuracy *= s.getAccuracyMultiplier();
        }

        return new AttachmentStats(recoil, damage, fireRate, accuracy);
    }

    /** ダメージ計算（武器・アタッチメント補正） */
    protected double calculateDamage(double baseDamage) {
        if (baseStats != null && attachmentStats != null) {
            return baseDamage * attachmentStats.getDamageMultiplier();
        }
        return baseDamage;
    }

    // =========================
    //   毎Tick処理
    // =========================

    /** 毎Tick呼ばれる処理（デフォルト実装） */
    public void onTick() {
        if (isExpired()) return;

        // 移動
        Vector velocity = getDirection().clone().multiply(getSpeed());
        setPosition(getPosition().add(velocity));

        World world = getShooter().getWorld();
        Location loc = new Location(world, getPosition().getX(), getPosition().getY(), getPosition().getZ());

        // 弾のパーティクル
        world.spawnParticle(Particle.CRIT, loc, 1, 0, 0, 0, 0);

        // 地面に到達したら消滅
        if (getPosition().getY() <= 0) {
            expire();
        }

        // TODO: ブロック・プレイヤー衝突判定
    }
    
    /** 弾を消滅させる */
    public void expire() {
        if (!expired) {
            expired = true;
            onRemove();
        }
    }
    
    /** 弾が消滅済みか */
    public boolean isExpired() {
        return expired;
    }

    /** 弾消滅時の処理（パーティクル・音など） */
    public void onRemove() {
        if (getShooter() == null) return;
        World world = getShooter().getWorld();
        Location loc = new Location(world, getPosition().getX(), getPosition().getY(), getPosition().getZ());

        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.1, 0.1, 0.1, 0.05);
        world.playSound(loc, Sound.ENTITY_ARROW_HIT, 1.0f, 1.0f);
    }


    // =========================
    //   Getter / Setter
    // =========================

    /** 弾ID */
    public String getId() {
        return id;
    }

    /** 射手 */
    public Player getShooter() {
        return shooter;
    }

    /** 射手を変更（API用） */
    public void setShooter(Player shooter) {
        if (shooter != null) this.shooter = shooter;
    }

    /** 現在位置（Vector） */
    public Vector getPosition() {
        return position.clone();
    }

    /** 現在位置を変更（Vector） */
    public void setPosition(Vector position) {
        if (position != null) this.position = position.clone();
    }

    /** 現在位置を変更（Bukkit Location） */
    public void setLocation(Location loc) {
        if (loc != null) this.position = loc.toVector();
    }

    /** 方向 */
    public Vector getDirection() {
        return direction.clone();
    }

    /** 方向設定 */
    public void setDirection(Vector direction) {
        if (direction != null && direction.length() > 0) {
            this.direction = direction.clone().normalize();
        }
    }

    /** 弾速度 */
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed > 0) this.speed = speed;
    }

    /** 発射後経過時間(ms) */
    public long getElapsedMillis() {
        return System.currentTimeMillis() - spawnTick;
    }

    /** 武器のベースダメージ */
    public double getBaseWeaponDamage() {
        return baseStats != null ? baseStats.getDamage() : 5.0;
    }

    /** 武器 + アタッチメント補正を含むダメージ */
    public double getModifiedDamage() {
        return calculateDamage(getBaseWeaponDamage());
    }
}
