package com.crackshotv3.core.weapon;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

/**
 * Weapon オブジェクト生成
 *
 * 修正点:
 * stats.reloadTime を getDouble → getInt に変更（WeaponStats.reloadTime が int になったため）
 */
public class WeaponFactory {

    public static Weapon createFromConfig(FileConfiguration config) {

        String id = config.getString("id");
        String name = config.getString("displayName", id);
        Material mat = Material.matchMaterial(config.getString("material", "STONE"));

        WeaponStats stats = new WeaponStats();
        stats.setDamage(config.getDouble("stats.damage", 10));
        stats.setHeadshotMultiplier(config.getDouble("stats.headshotMultiplier", 2));
        stats.setFireRate(config.getDouble("stats.fireRate", 0.1));
        stats.setProjectileSpeed(config.getDouble("stats.projectileSpeed", 3));
        stats.setRecoilVertical(config.getDouble("stats.recoilVertical", 0.5));
        stats.setRecoilHorizontal(config.getDouble("stats.recoilHorizontal", 0.3));
        stats.setRecoilRecoverySpeed(config.getDouble("stats.recoilRecoverySpeed", 0.1));
        stats.setMagazineSize(config.getInt("stats.magazineSize", 30));
        stats.setReserveAmmo(config.getInt("stats.reserveAmmo", 120));
        stats.setAutomatic(config.getBoolean("stats.automatic", true));
        stats.setBurst(config.getBoolean("stats.burst", false));
        stats.setBurstCount(config.getInt("stats.burstCount", 3));
        // 修正: getInt で読む (double → int に統一)
        stats.setReloadTime(config.getInt("stats.reloadTime", 40));

        stats.setAdsOffset(new Vector(
                config.getDouble("stats.adsOffset.x", 0),
                config.getDouble("stats.adsOffset.y", 0),
                config.getDouble("stats.adsOffset.z", 0)
        ));

        stats.setRecoilPattern(String.valueOf(config.getStringList("recoil.pattern")));

        stats.setProjectileType(config.getString("projectile.type", "hitscan"));
        stats.setProjectileGravity(config.getDouble("projectile.gravity", -0.03));
        stats.setProjectileRange(config.getDouble("projectile.range", 100));

        Weapon w = new Weapon(id, name, mat, stats);

        for (String id2 : config.getStringList("attachments.allowed")) {
            w.addAttachmentId(id2);
        }

        w.setScopeEnabled(config.getBoolean("scope.enabled", false));
        w.setScopeZoom((float) config.getDouble("scope.zoom", 30));
        w.setAdsSpeed((float) config.getDouble("scope.adsSpeed", 0.2));

        return w;
    }
}