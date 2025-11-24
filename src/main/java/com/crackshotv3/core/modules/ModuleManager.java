package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.util.LoggerUtil;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponRegistry;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ModuleManager
 *
 * - 武器に紐づくモジュールを動的に管理
 * - 発射・リロード・Tick などのイベントをモジュールへ分配
 * - 武器ごとに異なるモジュール構成を持てる柔軟設計
 * - 高速 & スレッドセーフ
 *
 * 追加機能:
 * - initialize(plugin): デフォルトモジュールを全武器に登録
 * - reload(plugin): clearAll() + initialize(plugin)
 * - Attachment 管理: getAttachmentById / registerAttachment
 */
public final class ModuleManager {

    /** 武器ID -> 登録されたモジュールのリスト */
    private final Map<String, List<Module>> moduleMap = new ConcurrentHashMap<>();

    /** Attachment ID -> Attachment オブジェクト */
    private final Map<String, Attachment> attachmentMap = new ConcurrentHashMap<>();

    /** Singleton */
    private static final ModuleManager INSTANCE = new ModuleManager();

    public static ModuleManager get() {
        return INSTANCE;
    }

    private ModuleManager() {
        LoggerUtil.debug("ModuleManager initialized.");
    }

    // =========================================================
    //  INITIALIZE / RELOAD / CLEAR
    // =========================================================

    /**
     * 初期化：既存の登録をクリアし、全武器にデフォルトモジュール群を割り当てる。
     * 将来的に weapon の設定に応じたモジュール割当をここで行う。
     */
    public synchronized void initialize(CrackShotV3 plugin) {
        LoggerUtil.info("[ModuleManager] Initializing modules...");

        // 既存を完全クリア
        clearAll();

        // デフォルトモジュールのインスタンスを作成（プラグイン渡し）
        Module shooting = new ShootingModule(plugin);
        Module reload = new ReloadModule(plugin);
        Module scope = new ScopeModule(plugin);
        Module heat = new HeatModule(plugin);
        Module jam = new JamModule(plugin);
        Module ammo = new AmmoModule(plugin);
        Module burst = new BurstModule(plugin);

        // 武器レジストリに登録されている全武器へデフォルトでモジュールを割り当てる
        Collection<Weapon> weapons = WeaponRegistry.get().getRegisteredWeapons();
        if (weapons == null || weapons.isEmpty()) {
            LoggerUtil.warn("[ModuleManager] No weapons found in registry to initialize modules.");
            return;
        }

        for (Weapon w : weapons) {
            String wid = w.getId();

            // 武器ごとに新しいモジュールを作成して登録
            registerModule(wid, new ShootingModule(plugin));
            registerModule(wid, new ReloadModule(plugin));
            registerModule(wid, new ScopeModule(plugin));
            registerModule(wid, new HeatModule(plugin));
            registerModule(wid, new JamModule(plugin));
            registerModule(wid, new AmmoModule(plugin));
            registerModule(wid, new BurstModule(plugin));

            LoggerUtil.debug("[ModuleManager] Default modules registered for weapon: " + wid);
        }

        LoggerUtil.info("[ModuleManager] Initialize complete. Weapons configured: " + weapons.size());
    }

    /**
     * リロード：簡潔に initialize を再実行する
     */
    public synchronized void reload(CrackShotV3 plugin) {
        LoggerUtil.info("[ModuleManager] Reloading modules...");
        initialize(plugin);
        LoggerUtil.info("[ModuleManager] Reload complete.");
    }

    /**
     * すべてのモジュール・Attachment登録をクリアする
     */
    public synchronized void clearAll() {
        moduleMap.clear();
        attachmentMap.clear();
        LoggerUtil.debug("[ModuleManager] All modules and attachments cleared.");
    }

    // =========================================================
    //  MODULE REGISTRATION
    // =========================================================

    /**
     * 指定武器にモジュールを登録
     */
    public synchronized void registerModule(String weaponId, Module module) {
        Objects.requireNonNull(weaponId, "weaponId");
        Objects.requireNonNull(module, "module");

        moduleMap.computeIfAbsent(weaponId, k -> new ArrayList<>()).add(module);
        LoggerUtil.debug("[ModuleManager] Registered module " + module.getClass().getSimpleName()
                + " for weapon " + weaponId);
    }

    /**
     * 指定武器の全モジュールを返す（読み取り専用リスト）
     */
    public List<Module> getModules(String weaponId) {
        List<Module> list = moduleMap.get(weaponId);
        if (list == null) return Collections.emptyList();
        return Collections.unmodifiableList(list);
    }

    // =========================================================
    //  ATTACHMENT REGISTRATION
    // =========================================================

    /**
     * Attachment を登録
     */
    public synchronized void registerAttachment(Attachment attachment) {
        if (attachment == null || attachment.getId() == null) return;
        attachmentMap.put(attachment.getId(), attachment);
        LoggerUtil.debug("[ModuleManager] Registered attachment: " + attachment.getId());
    }

    /**
     * ID から Attachment を取得
     */
    public Attachment getAttachmentById(String id) {
        if (id == null) return null;
        return attachmentMap.get(id);
    }

    /**
     * 全 Attachment をクリア
     */
    public synchronized void clearAttachments() {
        attachmentMap.clear();
        LoggerUtil.debug("[ModuleManager] All attachments cleared.");
    }

    // =========================================================
    //  DISPATCH EVENTS
    // =========================================================

    public void dispatchShoot(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        String id = weapon.getId();

        for (Module module : getModules(id)) {
            try { module.onShoot(player, weapon); }
            catch (Exception ex) {
                LoggerUtil.error("[ModuleManager] Error in onShoot: " + module.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
    }

    public void dispatchReload(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        String id = weapon.getId();

        for (Module module : getModules(id)) {
            try { module.onReload(player, weapon); }
            catch (Exception ex) {
                LoggerUtil.error("[ModuleManager] Error in onReload: " + module.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
    }

    public void dispatchTick(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        String id = weapon.getId();

        for (Module module : getModules(id)) {
            try { module.onTick(player, weapon); }
            catch (Exception ex) {
                LoggerUtil.error("[ModuleManager] Error in onTick: " + module.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
    }

    // =========================================================
    //  TRIGGERS FOR WEAPON.JAVA CALLS
    // =========================================================

    public void triggerReloadStart(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        String id = weapon.getId();

        for (Module module : getModules(id)) {
            try { module.onReloadStart(player, weapon); }
            catch (Exception ex) {
                LoggerUtil.error("[ModuleManager] Error in onReloadStart: " + module.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
    }

    public void triggerReloadFinish(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        String id = weapon.getId();

        for (Module module : getModules(id)) {
            try { module.onReloadFinish(player, weapon); }
            catch (Exception ex) {
                LoggerUtil.error("[ModuleManager] Error in onReloadFinish: " + module.getClass().getSimpleName());
                ex.printStackTrace();
            }
        }
    }

    public void triggerShoot(Player player, Weapon weapon) {
        dispatchShoot(player, weapon);
    }

    // =========================================================
    //  UTILITIES
    // =========================================================

    /**
     * デバッグ用：武器が持つ全モジュールをログ出力
     */
    public void debugListModules(String weaponId) {
        List<Module> modules = (List<Module>) getModules(weaponId);
        LoggerUtil.debug("=== Modules for weapon: " + weaponId + " ===");
        for (Module m : modules) {
            LoggerUtil.debug(" - " + m.getClass().getSimpleName());
        }
    }

    /**
     * デバッグ用：登録されている全 Attachment をログ出力
     */
    public void debugListAttachments() {
        LoggerUtil.debug("=== Registered Attachments ===");
        for (Attachment a : attachmentMap.values()) {
            LoggerUtil.debug(" - " + a.getId());
        }
    }
}
