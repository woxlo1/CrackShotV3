package com.crackshotv3.core.weapon;

import com.crackshotv3.core.attachments.AttachmentStats;
import org.bukkit.util.Vector;

/**
 * 武器の性能データ
 *
 * 修正点:
 * reloadTime を double から int に変更。
 * WeaponFactory では config.getInt("stats.reloadTime", 40) で読んでおり、
 * WeaponSaver では double として保存していたため次回ロード時に精度が失われていた。
 * tick 単位の整数で統一する。
 */
public class WeaponStats {

    // ===== ダメージ =====
    private double damage;
    private double headshotMultiplier;

    // ===== 射撃速度・弾速 =====
    private double fireRate;
    private double projectileSpeed;
    private String projectileType;
    private double projectileGravity;
    private double projectileRange;

    // ===== リコイル =====
    private double recoilVertical;
    private double recoilHorizontal;
    private double recoilRecoverySpeed;
    private String recoilPattern;

    // ===== 弾薬関連 =====
    private int magazineSize;
    private int reserveAmmo;

    // ===== バースト・その他 =====
    private boolean automatic;
    private boolean burst;
    private int burstCount;
    private Vector adsOffset;

    // 修正: double → int (tick単位で統一)
    private int reloadTime;

    // ===== スコープ =====
    private boolean scopeEnabled;
    private float scopeZoomFOV;
    private float adsSpeed;

    private WeaponStats baseStats;

    public WeaponStats() {
        this.damage = 10.0;
        this.headshotMultiplier = 2.0;
        this.fireRate = 0.2;
        this.projectileSpeed = 3.0;
        this.projectileType = "hitscan";
        this.projectileGravity = 0.0;
        this.projectileRange = 100.0;

        this.recoilVertical = 1.0;
        this.recoilHorizontal = 0.5;
        this.recoilRecoverySpeed = 0.1;
        this.recoilPattern = "default";

        this.magazineSize = 30;
        this.reserveAmmo = 90;

        this.automatic = true;
        this.burst = false;
        this.burstCount = 3;
        this.adsOffset = new Vector(0, 0, 0);
        this.reloadTime = 40; // 修正: int (ticks)

        this.scopeEnabled = false;
        this.scopeZoomFOV = 30f;
        this.adsSpeed = 0.2f;
    }

    // ===== リセット =====
    public void resetToBase() {
        if (baseStats == null) baseStats = this.copy();
        WeaponStats b = baseStats;

        this.damage = b.damage;
        this.headshotMultiplier = b.headshotMultiplier;
        this.fireRate = b.fireRate;
        this.projectileSpeed = b.projectileSpeed;
        this.projectileType = b.projectileType;
        this.projectileGravity = b.projectileGravity;
        this.projectileRange = b.projectileRange;

        this.recoilVertical = b.recoilVertical;
        this.recoilHorizontal = b.recoilHorizontal;
        this.recoilRecoverySpeed = b.recoilRecoverySpeed;
        this.recoilPattern = b.recoilPattern;

        this.magazineSize = b.magazineSize;
        this.reserveAmmo = b.reserveAmmo;

        this.automatic = b.automatic;
        this.burst = b.burst;
        this.burstCount = b.burstCount;
        this.adsOffset = b.adsOffset.clone();
        this.reloadTime = b.reloadTime;

        this.scopeEnabled = b.scopeEnabled;
        this.scopeZoomFOV = b.scopeZoomFOV;
        this.adsSpeed = b.adsSpeed;
    }

    // ===== AttachmentStats を反映 =====
    public void applyModifier(AttachmentStats stats) {
        if (stats == null) return;
        this.damage *= stats.getDamageMultiplier();
        this.fireRate *= stats.getFireRateMultiplier();
        this.recoilVertical *= stats.getRecoilMultiplier();
        this.recoilHorizontal *= stats.getRecoilMultiplier();
        this.projectileSpeed *= stats.getAccuracyMultiplier();
    }

    // ===== コピー =====
    public WeaponStats copy() {
        WeaponStats copy = new WeaponStats();
        copy.damage = this.damage;
        copy.headshotMultiplier = this.headshotMultiplier;
        copy.fireRate = this.fireRate;
        copy.projectileSpeed = this.projectileSpeed;
        copy.projectileType = this.projectileType;
        copy.projectileGravity = this.projectileGravity;
        copy.projectileRange = this.projectileRange;

        copy.recoilVertical = this.recoilVertical;
        copy.recoilHorizontal = this.recoilHorizontal;
        copy.recoilRecoverySpeed = this.recoilRecoverySpeed;
        copy.recoilPattern = this.recoilPattern;

        copy.magazineSize = this.magazineSize;
        copy.reserveAmmo = this.reserveAmmo;

        copy.automatic = this.automatic;
        copy.burst = this.burst;
        copy.burstCount = this.burstCount;
        copy.adsOffset = this.adsOffset.clone();
        copy.reloadTime = this.reloadTime;

        copy.scopeEnabled = this.scopeEnabled;
        copy.scopeZoomFOV = this.scopeZoomFOV;
        copy.adsSpeed = this.adsSpeed;

        return copy;
    }

    // ===== Getter / Setter =====
    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }

    public double getHeadshotMultiplier() { return headshotMultiplier; }
    public void setHeadshotMultiplier(double headshotMultiplier) { this.headshotMultiplier = headshotMultiplier; }

    public double getFireRate() { return fireRate; }
    public void setFireRate(double fireRate) { this.fireRate = fireRate; }

    public double getProjectileSpeed() { return projectileSpeed; }
    public void setProjectileSpeed(double projectileSpeed) { this.projectileSpeed = projectileSpeed; }

    public String getProjectileType() { return projectileType; }
    public void setProjectileType(String projectileType) {
        if (projectileType != null && !projectileType.isEmpty())
            this.projectileType = projectileType.toLowerCase();
    }

    public double getProjectileGravity() { return projectileGravity; }
    public void setProjectileGravity(double projectileGravity) { this.projectileGravity = projectileGravity; }

    public double getProjectileRange() { return projectileRange; }
    public void setProjectileRange(double projectileRange) { this.projectileRange = projectileRange; }

    public double getRecoilVertical() { return recoilVertical; }
    public void setRecoilVertical(double recoilVertical) { this.recoilVertical = recoilVertical; }

    public double getRecoilHorizontal() { return recoilHorizontal; }
    public void setRecoilHorizontal(double recoilHorizontal) { this.recoilHorizontal = recoilHorizontal; }

    public double getRecoilRecoverySpeed() { return recoilRecoverySpeed; }
    public void setRecoilRecoverySpeed(double recoilRecoverySpeed) { this.recoilRecoverySpeed = recoilRecoverySpeed; }

    public String getRecoilPattern() { return recoilPattern; }
    public void setRecoilPattern(String recoilPattern) { this.recoilPattern = recoilPattern; }

    public int getMagazineSize() { return magazineSize; }
    public void setMagazineSize(int magazineSize) { this.magazineSize = magazineSize; }

    public int getReserveAmmo() { return reserveAmmo; }
    public void setReserveAmmo(int reserveAmmo) { this.reserveAmmo = reserveAmmo; }

    public boolean isAutomatic() { return automatic; }
    public void setAutomatic(boolean automatic) { this.automatic = automatic; }

    public boolean isBurst() { return burst; }
    public void setBurst(boolean burst) { this.burst = burst; }

    public int getBurstCount() { return burstCount; }
    public void setBurstCount(int burstCount) { this.burstCount = burstCount; }

    public Vector getAdsOffset() { return adsOffset; }
    public void setAdsOffset(Vector adsOffset) { this.adsOffset = adsOffset; }

    // 修正: double → int
    public int getReloadTime() { return reloadTime; }
    public void setReloadTime(int reloadTime) { this.reloadTime = reloadTime; }

    public boolean isScopeEnabled() { return scopeEnabled; }
    public void setScopeEnabled(boolean scopeEnabled) { this.scopeEnabled = scopeEnabled; }

    public float getScopeZoomFOV() { return scopeZoomFOV; }
    public void setScopeZoomFOV(float scopeZoomFOV) { this.scopeZoomFOV = scopeZoomFOV; }

    public float getAdsSpeed() { return adsSpeed; }
    public void setAdsSpeed(float adsSpeed) { this.adsSpeed = adsSpeed; }
}