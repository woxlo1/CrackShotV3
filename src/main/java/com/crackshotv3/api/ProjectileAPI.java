package com.crackshotv3.api;

import com.crackshotv3.core.projectile.ProjectileBase;
import com.crackshotv3.core.projectile.ProjectileEngine;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * ProjectileAPI
 * 弾丸生成・発射・ヒット管理用API
 */
public class ProjectileAPI {

    private final ProjectileEngine engine;

    // プレイヤーごとのヒット情報管理
    private final Map<Player, Map<Player, Double>> hitMap = new HashMap<>();

    public ProjectileAPI(ProjectileEngine engine) {
        this.engine = engine;
    }

    // =====================================================
    // Hit管理
    // =====================================================

    /**
     * 弾丸がプレイヤーにヒットした際の登録
     *
     * @param shooter 射手
     * @param target 被弾者
     * @param damage 与えたダメージ
     */
    public void registerHit(Player shooter, Player target, double damage) {
        if (shooter == null || target == null || damage <= 0) return;

        hitMap.computeIfAbsent(shooter, k -> new HashMap<>());
        Map<Player, Double> shooterHits = hitMap.get(shooter);
        shooterHits.put(target, shooterHits.getOrDefault(target, 0.0) + damage);
    }

    /**
     * 射手が与えたダメージを取得
     */
    public double getTotalDamage(Player shooter, Player target) {
        if (shooter == null || target == null) return 0.0;
        return hitMap.getOrDefault(shooter, Map.of()).getOrDefault(target, 0.0);
    }

    /**
     * 射手のヒット履歴をリセット
     */
    public void clearHits(Player shooter) {
        if (shooter != null) hitMap.remove(shooter);
    }

    // =====================================================
    // 弾丸操作
    // =====================================================

    /**
     * 新規弾丸を生成して発射
     */
    public ProjectileBase spawnProjectile(Player player, Weapon weapon) {
        if (player == null || weapon == null) return null;

        // ProjectileEngine 側の factory メソッドに Player も渡す
        ProjectileBase projectile = ProjectileEngine.createProjectileFromWeapon(weapon, player);

        if (projectile == null) return null;

        // 発射位置と方向を設定
        Location loc = player.getEyeLocation();
        Vector direction = loc.getDirection();

        projectile.setShooter(player);
        projectile.setLocation(loc);
        projectile.setDirection(direction);

        // エンジンに登録して発射
        engine.addProjectile(projectile);

        return projectile;
    }

    /**
     * 弾丸の手動削除（Expire）
     */
    public void removeProjectile(ProjectileBase projectile) {
        if (projectile == null) return;
        projectile.expire();
        engine.removeProjectile(projectile);
    }
}
