package com.crackshotv3.core.projectile;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.util.LoggerUtil;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 全弾丸の Tick 管理
 * <p>
 * ・Tick ごとに弾を更新
 * ・手動で追加・削除可能
 * ・自動で消滅済み弾を削除
 */
public class ProjectileEngine {

    private static final List<ProjectileBase> activeProjectiles = new ArrayList<>();
    private static boolean initialized = false;
    private static CrackShotV3 plugin;
    private static BukkitRunnable tickTask;

    // ==========================
    // 初期化 / シャットダウン
    // ==========================
    public static void init(CrackShotV3 pl) {
        if (initialized) return;
        plugin = pl;

        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                tickAll();
            }
        };
        tickTask.runTaskTimer(plugin, 1L, 1L); // 1 tick ごと
        initialized = true;
        LoggerUtil.info("[ProjectileEngine] Initialized.");
    }

    public static void shutdown() {
        if (!initialized) return;

        if (tickTask != null) tickTask.cancel();
        activeProjectiles.clear();
        initialized = false;
        LoggerUtil.info("[ProjectileEngine] Shutdown complete.");
    }

    // ==========================
    // 弾丸管理 API
    // ==========================
    /**
     * Weapon から弾丸を生成
     */
    public static ProjectileBase createProjectileFromWeapon(Weapon weapon, Player shooter) {
        if (weapon == null || shooter == null) return null;

        String projectileId = weapon.getId() + "_bullet";

        // 匿名クラスでインスタンス化
        ProjectileBase projectile = new ProjectileBase(
                projectileId,
                shooter,
                shooter.getLocation().toVector(),
                shooter.getLocation().getDirection()
        ) {
            @Override
            public void onTick() {
                super.onTick(); // デフォルトの挙動を呼ぶ
            }
        };

        projectile.setWeapon(weapon);

        return projectile;
    }
    /**
     * 弾を登録（addProjectile と同義）
     */
    public static void addProjectile(ProjectileBase projectile) {
        registerProjectile(projectile);
    }

    /**
     * 弾を削除（removeProjectile）
     */
    public static void removeProjectile(ProjectileBase projectile) {
        if (projectile == null) return;
        projectile.expire();
        activeProjectiles.remove(projectile);
        LoggerUtil.debug("[ProjectileEngine] Projectile manually removed: " + projectile.getId());
    }

    /**
     * 弾を登録
     */
    public static void registerProjectile(ProjectileBase projectile) {
        if (projectile == null) return;
        activeProjectiles.add(projectile);
        LoggerUtil.debug("[ProjectileEngine] Projectile registered: " + projectile.getId());
    }

    /**
     * アクティブ弾丸の取得
     */
    public static List<ProjectileBase> getActiveProjectiles() {
        return new ArrayList<>(activeProjectiles);
    }

    public static List<ProjectileBase> getProjectiles() {
        return new ArrayList<>(activeProjectiles);
    }


    // ==========================
    // 弾丸 Tick 処理
    // ==========================
    private static void tickAll() {
        if (activeProjectiles.isEmpty()) return;

        Iterator<ProjectileBase> iterator = activeProjectiles.iterator();
        while (iterator.hasNext()) {
            ProjectileBase proj = iterator.next();
            try {
                proj.onTick();

                if (proj.isExpired()) {
                    proj.onRemove();      // 弾消滅時のフック
                    iterator.remove();    // リストから削除
                    LoggerUtil.debug("[ProjectileEngine] Projectile removed: " + proj.getId());
                }
            } catch (Exception e) {
                LoggerUtil.error("[ProjectileEngine] Error ticking projectile: " + proj.getId(), e);
                iterator.remove(); // エラー発生時も削除
            }
        }
    }

}
