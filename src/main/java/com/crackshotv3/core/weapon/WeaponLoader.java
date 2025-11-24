package com.crackshotv3.core.weapon;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * 武器データ読み込み
 */
public class WeaponLoader {

    private static final String WEAPON_FOLDER = "weapons";

    public static void loadAll(CrackShotV3 plugin) {
        File folder = new File(plugin.getDataFolder(), WEAPON_FOLDER);
        if (!folder.exists()) folder.mkdirs();

        WeaponRegistry.get().clear();

        File[] files = folder.listFiles((dir, name) ->
                name.endsWith(".yml") || name.endsWith(".yaml")
        );
        if (files == null) return;

        for (File file : files) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                Weapon weapon = WeaponFactory.createFromConfig(config);

                if (weapon == null || weapon.getId() == null) {
                    LoggerUtil.warn("[WeaponLoader] Invalid weapon file: " + file.getName());
                    continue;
                }

                WeaponRegistry.get().registerWeapon(weapon);
                LoggerUtil.info("[WeaponLoader] Loaded weapon: " + weapon.getId());

            } catch (Exception e) {
                LoggerUtil.error("[WeaponLoader] Failed to load weapon: " + file.getName(), e);
            }
        }
    }
}
