package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.projectile.PhysicalProjectile;
import com.crackshotv3.core.projectile.ProjectileBase;
import com.crackshotv3.core.projectile.ProjectileEngine;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

import static com.crackshotv3.core.util.MessageUtil.send;

/**
 * 単発射撃処理。
 */
public class ShootingModule extends Module {

    public ShootingModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "ShootingModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;

        // 武器がジャム・オーバーヒートしている場合は発射不可
        try {
            if (weapon.isJammed() || weapon.isOverheated()) {
                send(player, "§c発射できません");
                return;
            }
        } catch (Throwable ignored) {
            // Weapon にそのメソッドが無い場合はスルー（互換性確保）
        }

        try {
            // プレイヤーの目線位置と向き
            Location eye = player.getEyeLocation();
            Vector position = eye.toVector();
            Vector direction = eye.getDirection().normalize();

            // 一意な ID を付与（weaponId-UUID）
            String projId = weapon.getId() + "-" + UUID.randomUUID().toString();

            // TODO: 将来的には weapon.getStats() を参照して
            // HitScan / Beam / Explosive などに切り替えられるようにする
            ProjectileBase projectile = new PhysicalProjectile(projId, player, position, direction);

            // 登録してエンジンで管理させる
            ProjectileEngine.registerProjectile(projectile);

            debug(player.getName() + " fired " + weapon.getId() + " -> projectile=" + projId);

        } catch (Exception e) {
            LoggerUtil.error("[ShootingModule] Failed to spawn projectile for player: " + player.getName(), e);
            send(player, "§c発射に失敗しました (内部エラー)");
        }
    }
}
