package com.crackshotv3;

import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.api.AttachmentAPI;
import com.crackshotv3.api.ProjectileAPI;
import com.crackshotv3.api.AnimationAPI;
import com.crackshotv3.core.attachments.AttachmentManager;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.core.projectile.ProjectileEngine;
import com.crackshotv3.core.animation.AnimationEngine;
import com.crackshotv3.core.attachments.AttachmentRegistry;
import com.crackshotv3.listeners.*;
import com.crackshotv3.commands.*;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrackShotV3 extends JavaPlugin {

    private static CrackShotV3 instance;

    private V3Bootstrap bootstrap;

    // API
    private WeaponAPI weaponAPI;
    private ProjectileAPI projectileAPI;
    private AttachmentAPI attachmentAPI;
    private AnimationAPI animationAPI;

    public static CrackShotV3 getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();

        LoggerUtil.init(this, getConfig().getBoolean("debug", false));
        LoggerUtil.info("=======================================");
        LoggerUtil.info("       CrackShotV3 Loading...");
        LoggerUtil.info("=======================================");
    }

    @Override
    public void onEnable() {
        LoggerUtil.init(this, getConfig().getBoolean("debug", false));
        LoggerUtil.info("=======================================");
        LoggerUtil.info("       CrackShotV3 Enabling...");
        LoggerUtil.info("=======================================");

        long start = System.currentTimeMillis();

        bootstrap = new V3Bootstrap(this);
        bootstrap.initialize();

        // ProjectileEngine は bootstrap.initialize() 内で init() 済みなので
        // ここで getInstance() が使える
        createAPIInstances();
        registerCommands();
        registerListeners();

        long took = System.currentTimeMillis() - start;
        LoggerUtil.info("CrackShotV3 Enabled! (" + took + "ms)");
    }

    @Override
    public void onDisable() {
        LoggerUtil.info("CrackShotV3 shutting down...");
        if (bootstrap != null) bootstrap.shutdown();
        LoggerUtil.info("CrackShotV3 Disabled.");
    }

    // ==== Commands ====
    private void registerCommands() {
        getCommand("weapon").setExecutor(new WeaponCommand());
        getCommand("weaponreload").setExecutor(new ReloadCommand());
        getCommand("attachment").setExecutor(new AttachmentCommand());
        getCommand("csv3debug").setExecutor(new DebugCommand());
        LoggerUtil.debug("Commands registered.");
    }

    // ==== Listeners ====
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerShootListener(weaponAPI, projectileAPI), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHitListener(projectileAPI), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSwapHandListener(weaponAPI), this);
        // PlayerInteractListener は PlayerShootListener と処理が重複するため削除
        Bukkit.getPluginManager().registerEvents(new PlayerDropListener(weaponAPI), this);
        Bukkit.getPluginManager().registerEvents(new PlayerZoomListener(weaponAPI), this);
    }

    // ==== API ====
    private void createAPIInstances() {
        weaponAPI = new WeaponAPI(WeaponRegistry.get());

        // 修正: null ではなく ProjectileEngine.getInstance() を渡す
        projectileAPI = new ProjectileAPI(ProjectileEngine.getInstance());

        attachmentAPI = new AttachmentAPI(AttachmentManager.get());
        animationAPI = new AnimationAPI(AnimationEngine.get());
        LoggerUtil.debug("API instances created.");
    }

    public WeaponAPI getWeaponAPI() { return weaponAPI; }
    public ProjectileAPI getProjectileAPI() { return projectileAPI; }
    public AttachmentAPI getAttachmentAPI() { return attachmentAPI; }
    public AnimationAPI getAnimationAPI() { return animationAPI; }
}