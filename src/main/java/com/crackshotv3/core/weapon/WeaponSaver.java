package com.crackshotv3.core.weapon;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * 武器データ保存（GUI編集用）
 *
 * 修正点:
 * reloadTime を double で保存していたが WeaponStats.getReloadTime() が int を返すようになったため
 * config.set("stats.reloadTime", s.getReloadTime()) で int が保存される。
 * WeaponFactory 側の getInt() と一致する。
 */
public class WeaponSaver {

    private static final String WEAPON_FOLDER = "weapons";

    public static void saveWeapon(CrackShotV3 plugin, Weapon weapon) {

        File folder = new File(plugin.getDataFolder(), WEAPON_FOLDER);
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, weapon.getId() + ".yml");

        YamlConfiguration config = new YamlConfiguration();

        // 基本
        config.set("id", weapon.getId());
        config.set("displayName", weapon.getDisplayName());
        config.set("material", weapon.getMaterial().name());

        // スコープ
        config.set("scope.enabled", weapon.isScopeEnabled());
        config.set("scope.zoom", weapon.getScopeZoom());
        config.set("scope.adsSpeed", weapon.getAdsSpeed());

        // Stats
        WeaponStats s = weapon.getStats();
        config.set("stats.damage", s.getDamage());
        config.set("stats.headshotMultiplier", s.getHeadshotMultiplier());
        config.set("stats.fireRate", s.getFireRate());
        config.set("stats.projectileSpeed", s.getProjectileSpeed());
        config.set("stats.recoilVertical", s.getRecoilVertical());
        config.set("stats.recoilHorizontal", s.getRecoilHorizontal());
        config.set("stats.recoilRecoverySpeed", s.getRecoilRecoverySpeed());
        config.set("stats.magazineSize", s.getMagazineSize());
        config.set("stats.reserveAmmo", s.getReserveAmmo());
        config.set("stats.automatic", s.isAutomatic());
        config.set("stats.burst", s.isBurst());
        config.set("stats.burstCount", s.getBurstCount());
        // 修正: int として保存 (以前は double で保存していた)
        config.set("stats.reloadTime", s.getReloadTime());

        // ads offset
        config.set("stats.adsOffset.x", s.getAdsOffset().getX());
        config.set("stats.adsOffset.y", s.getAdsOffset().getY());
        config.set("stats.adsOffset.z", s.getAdsOffset().getZ());

        // 反動パターン
        config.set("recoil.pattern", s.getRecoilPattern());

        // projectile
        config.set("projectile.type", s.getProjectileType());
        config.set("projectile.gravity", s.getProjectileGravity());
        config.set("projectile.range", s.getProjectileRange());

        // アタッチメント
        config.set("attachments.allowed", weapon.getAttachmentIds());

        try {
            config.save(file);
            LoggerUtil.info("[WeaponSaver] Saved weapon: " + weapon.getId());
        } catch (IOException e) {
            LoggerUtil.error("[WeaponSaver] Failed to save weapon: " + weapon.getId(), e);
        }
    }
}