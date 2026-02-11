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
 *
 * 修正点:
 * 1. getInstance() を追加 (CrackShotV3.java から渡せるようにする)
 * 2. tickAll() 内で onRemove() を二重呼び出ししていた問題を修正
 *    - expire() の中で既に onRemove() が呼ばれるため、tickAll() では呼ばない
 */
public class ProjectileEngine {

    private static final List<ProjectileBase> activeProjectiles = new ArrayList<>();
    private static boolean initialized = false;
    private static CrackShotV3 plugin;
    private static BukkitRunnable tickTask;

    // 修正: Singleton インスタンス
    private static ProjectileEngine INSTANCE;

    // ==========================
    // 初期化 / シャットダウン
    // ==========================
    public static void init(CrackShotV3 pl) {
        if (initialized) return;
        plugin = pl;
        INSTANCE = new ProjectileEngine();

        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                tickAll();
            }
        };
        tickTask.runTaskTimer(plugin, 1L, 1L);
        initialized = true;
        LoggerUtil.info("[ProjectileEngine] Initialized.");
    }

    /**
     * 修正: CrackShotV3.java から ProjectileAPI へ渡せるよう getInstance() を追加
     */
    public static ProjectileEngine getInstance() {
        return INSTANCE;
    }

    public static void shutdown() {
        if (!initialized) return;

        if (tickTask != null) tickTask.cancel();
        activeProjectiles.clear();
        initialized = false;
        INSTANCE = null;
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

        ProjectileBase projectile = new ProjectileBase(
                projectileId,
                shooter,
                shooter.getLocation().toVector(),
                shooter.getLocation().getDirection()
        ) {
            @Override
            public void onTick() {
                super.onTick();
            }
        };

        projectile.setWeapon(weapon);
        return projectile;
    }

    /**
     * 弾を登録 (addProjectile と同義)
     */
    public static void addProjectile(ProjectileBase projectile) {
        registerProjectile(projectile);
    }

    /**
     * インスタンスメソッド版 addProjectile (ProjectileAPI から呼ぶ)
     */
    public void addProjectileInstance(ProjectileBase projectile) {
        registerProjectile(projectile);
    }

    /**
     * インスタンスメソッド版 removeProjectile (ProjectileAPI から呼ぶ)
     */
    public void removeProjectileInstance(ProjectileBase projectile) {
        removeProjectile(projectile);
    }

    /**
     * 弾を削除
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
                    // 修正: expire() 内で既に onRemove() が呼ばれているため、ここでは呼ばない
                    // 旧コード: proj.onRemove(); ← 二重呼び出しだった
                    iterator.remove();
                    LoggerUtil.debug("[ProjectileEngine] Projectile removed: " + proj.getId());
                }
            } catch (Exception e) {
                LoggerUtil.error("[ProjectileEngine] Error ticking projectile: " + proj.getId(), e);
                iterator.remove();
            }
        }
    }
}