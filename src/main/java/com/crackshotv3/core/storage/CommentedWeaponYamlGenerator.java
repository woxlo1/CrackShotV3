package com.crackshotv3.core.storage;

import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponStats;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Weapon をコメント付き YAML で出力する補助
 */
public final class CommentedWeaponYamlGenerator {

    public static void generate(File pluginDataFolder, Weapon weapon) {
        File folder = new File(pluginDataFolder, "weapons");
        if (!folder.exists()) folder.mkdirs();

        File target = new File(folder, weapon.getId() + ".yml");
        try {
            YamlConfiguration cfg = new YamlConfiguration();

            cfg.set("id", weapon.getId());
            cfg.set("displayName", weapon.getDisplayName());
            cfg.set("material", weapon.getItemMaterial().name());

            WeaponStats s = weapon.getStats();
            cfg.set("stats.damage", s.getDamage());
            cfg.set("stats.headshotMultiplier", s.getHeadshotMultiplier());
            cfg.set("stats.fireRate", s.getFireRate());
            cfg.set("stats.projectileType", s.getProjectileType());
            cfg.set("stats.projectileSpeed", s.getProjectileSpeed());
            cfg.set("stats.recoilVertical", s.getRecoilVertical());
            cfg.set("stats.recoilHorizontal", s.getRecoilHorizontal());
            cfg.set("stats.magazineSize", s.getMagazineSize());

            File tmp = new File(folder, weapon.getId() + ".yml.tmp");
            cfg.save(tmp);
            List<String> lines = java.nio.file.Files.readAllLines(tmp.toPath());

            StringBuilder sb = new StringBuilder();
            sb.append("# CrackShotV3 weapon configuration\n");
            sb.append("# --------------------------------\n");
            sb.append("# This file is auto-generated with comments to help server admins\n");
            sb.append("# - id: internal identifier (change carefully)\n");
            sb.append("# - displayName: shown to players\n");
            sb.append("# - material: Minecraft material used as representation\n");
            sb.append("# - stats.*: core weapon parameters\n\n");

            for (String line : lines) {
                if (line.startsWith("id:")) {
                    sb.append("# internal unique id (string)\n");
                } else if (line.startsWith("displayName:")) {
                    sb.append("# display name shown to players (string)\n");
                } else if (line.startsWith("material:")) {
                    sb.append("# Minecraft Material (e.g. IRON_SWORD). Use uppercase names.\n");
                } else if (line.contains("stats.damage")) {
                    sb.append("# damage: base hit damage (double)\n");
                } else if (line.contains("stats.fireRate")) {
                    sb.append("# fireRate: seconds or ticks between shots (double)\n");
                } else if (line.contains("stats.projectileType")) {
                    sb.append("# projectileType: hitscan|physical|explosive|piercing|beam\n");
                }
                sb.append(line).append("\n");
            }

            try (FileWriter fw = new FileWriter(target, false)) {
                fw.write(sb.toString());
            }

            tmp.delete();
            LoggerUtil.info("[YAMLGen] Generated commented YAML: " + target.getName());

        } catch (IOException e) {
            LoggerUtil.error("[YAMLGen] Failed to generate commented YAML for " + weapon.getId(), e);
        }
    }
}
