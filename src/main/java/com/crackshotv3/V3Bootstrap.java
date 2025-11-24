package com.crackshotv3;

import com.crackshotv3.core.weapon.WeaponLoader;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.core.attachments.AttachmentRegistry;
import com.crackshotv3.core.attachments.AttachmentLoader;
import com.crackshotv3.core.projectile.ProjectileEngine;
import com.crackshotv3.core.animation.AnimationEngine;
import com.crackshotv3.core.modules.ModuleManager;
import com.crackshotv3.core.util.LoggerUtil;

/**
 * CrackShotV3 の初期化ブートストラップクラス。
 */
public class V3Bootstrap {

    private final CrackShotV3 plugin;

    public V3Bootstrap(CrackShotV3 plugin) {
        this.plugin = plugin;
    }

    // ============================================================
    // Initialization
    // ============================================================

    /** onEnable() で呼ぶ、完全初期化 */
    public void initialize() {
        LoggerUtil.info("========== CrackShotV3 Initializing ==========");

        initModules();
        loadWeaponData();
        loadAttachmentData();
        initProjectileEngine();
        initAnimationEngine();

        LoggerUtil.info("========== Initialization Complete ==========");
    }

    public void initModules() {
        ModuleManager.get().initialize(plugin);
        LoggerUtil.info("[Bootstrap] Modules initialized.");
    }

    public void loadWeaponData() {
        WeaponLoader.loadAll(plugin);
        LoggerUtil.info("[Bootstrap] Loaded weapons: " +
                WeaponRegistry.get().getRegisteredWeapons().size());
    }

    public void loadAttachmentData() {
        AttachmentLoader.loadAll(plugin);
        LoggerUtil.info("[Bootstrap] Loaded attachments: " +
                AttachmentRegistry.get().getAllAttachments().size());
    }

    public void initProjectileEngine() {
        ProjectileEngine.init(plugin);
        LoggerUtil.info("[Bootstrap] Projectile engine initialized.");
    }

    public void initAnimationEngine() {
        AnimationEngine.get().init(plugin);
        LoggerUtil.info("[Bootstrap] Animation engine initialized.");
    }

    // ============================================================
    // Hot Reload
    // ============================================================

    public void reloadAll() {
        LoggerUtil.info("========== CrackShotV3 Reload ==========");

        plugin.reloadConfig();

        WeaponLoader.loadAll(plugin);
        AttachmentLoader.loadAll(plugin);

        ModuleManager.get().reload(plugin);

        LoggerUtil.info("[Bootstrap] Reload completed.");
    }

    // ============================================================
    // Shutdown
    // ============================================================

    public void shutdown() {
        LoggerUtil.info("[Bootstrap] Shutting down...");

        try {
            ProjectileEngine.shutdown();
            AnimationEngine.get().shutdown();
        } catch (Exception e) {
            LoggerUtil.error("Error during shutdown", e);
        }

        LoggerUtil.info("[Bootstrap] Shutdown complete.");
    }
}
