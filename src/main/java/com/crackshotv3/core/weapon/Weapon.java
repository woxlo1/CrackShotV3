package com.crackshotv3.core.weapon;

import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentStats;
import com.crackshotv3.core.modules.ModuleManager;
import com.crackshotv3.core.recoil.RecoilPattern;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * 武器クラス（WeaponSaver 完全互換版）
 */
public class Weapon {

    private final String id;
    private String displayName;
    private Material itemMaterial;
    private WeaponStats stats;
    private RecoilPattern recoilPattern;
    private final List<Attachment> attachments;
    private WeaponState currentState;

    // --- モジュールが要求する追加フィールド ---
    private int ammo = 30;
    private int maxAmmo = 30;

    private boolean jammed = false;
    private double jamChance = 0.0;

    private boolean overheated = false;
    private double heat = 0.0;
    private double heatMax = 100.0;
    private double heatPerShot = 5.0;
    private double coolRate = 1.0;

    private int burstCount = 1;
    private int burstInterval = 1;

    private boolean hasScope = false;
    private float scopeZoomFOV = 30f;

    private boolean ammoSystem = true;
    private boolean heatSystem = true;
    private boolean jamSystem = true;

    public Weapon(String id, String displayName, Material itemMaterial, WeaponStats stats) {
        this.id = id;
        this.displayName = displayName;
        this.itemMaterial = itemMaterial;
        this.stats = stats;
        this.attachments = new ArrayList<>();
        this.currentState = new WeaponState();
    }

    // =====================================================
    // Getter / Setter
    // =====================================================
    public String getId() { return id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Material getItemMaterial() { return itemMaterial; }
    public Material getMaterial() { return itemMaterial; }
    public void setItemMaterial(Material itemMaterial) { this.itemMaterial = itemMaterial; }

    public WeaponStats getStats() { return stats; }
    public void setStats(WeaponStats stats) { this.stats = stats; }

    public RecoilPattern getRecoilPattern() { return recoilPattern; }
    public void setRecoilPattern(RecoilPattern recoilPattern) { this.recoilPattern = recoilPattern; }

    public WeaponState getCurrentState() { return currentState; }
    public void setCurrentState(WeaponState currentState) { this.currentState = currentState; }

    public List<Attachment> getAttachments() { return Collections.unmodifiableList(attachments); }

    // =====================================================
    // Ammo System
    // =====================================================
    public boolean hasAmmoSystem() { return ammoSystem; }
    public void setAmmoSystem(boolean enabled) { this.ammoSystem = enabled; }

    public int getAmmo() { return ammo; }
    public int getMaxAmmo() { return maxAmmo; }
    public void setMaxAmmo(int size) { this.maxAmmo = size; }

    public boolean consumeAmmo(Player player) {
        if (!ammoSystem) return true;
        if (ammo <= 0) {
            LoggerUtil.debug("[Weapon] Ammo empty for " + id);
            return false;
        }
        ammo--;
        return true;
    }

    public void reloadFull() { this.ammo = maxAmmo; }

    // =====================================================
    // Burst System
    // =====================================================
    public int getBurstCount() { return burstCount; }
    public void setBurstCount(int count) { this.burstCount = count; }

    public int getBurstInterval() { return burstInterval; }
    public void setBurstInterval(int interval) { this.burstInterval = interval; }

    // =====================================================
    // Jam System
    // =====================================================
    public boolean hasJamSystem() { return jamSystem; }
    public void setJamSystem(boolean enabled) { this.jamSystem = enabled; }

    public boolean isJammed() { return jammed; }
    public void setJammed(boolean jammed) { this.jammed = jammed; }

    public double getJamChance() { return jamChance; }
    public void setJamChance(double chance) { this.jamChance = chance; }

    // =====================================================
    // Heat System
    // =====================================================
    public boolean hasHeatSystem() { return heatSystem; }
    public void setHeatSystem(boolean enabled) { this.heatSystem = enabled; }

    public double getHeatPerShot() { return heatPerShot; }
    public void setHeatPerShot(double value) { this.heatPerShot = value; }

    public boolean isOverheated() { return overheated; }

    public void addHeat() { addHeat(heatPerShot); }
    public void addHeat(double amount) {
        heat += amount;
        if (heat >= heatMax) overheated = true;
    }

    public void coolDownHeat() {
        if (heat > 0) heat -= coolRate;
        if (heat < heatMax * 0.6) overheated = false;
    }

    // =====================================================
    // Scope System
    // =====================================================
    public boolean hasScope() { return hasScope; }
    public void setScope(boolean enabled) { this.hasScope = enabled; }

    public float getScopeZoomFOV() { return scopeZoomFOV; }
    public void setScopeZoomFOV(float fov) { this.scopeZoomFOV = fov; }

    private final Map<Player, Boolean> zoomingPlayers = new HashMap<>();

    // WeaponSaver 互換用
    public boolean isScopeEnabled() { return hasScope(); }
    public float getScopeZoom() { return getScopeZoomFOV(); }
    public float getAdsSpeed() { return stats != null ? stats.getAdsSpeed() : 0.2f; }
    public List<String> getAttachmentIds() {
        List<String> ids = new ArrayList<>();
        for (Attachment a : attachments) {
            ids.add(a.getId());
        }
        return ids;
    }

    // =====================================================
    // Reload System
    // =====================================================
    public boolean isReloading() { return currentState.isReloading(); }

    public void startReload(Player player) {
        if (isReloading()) return;
        currentState.setReloading(true);
        ModuleManager.get().triggerReloadStart(player, this);
        LoggerUtil.debug("[Weapon] Reload started by " + player.getName() + " for " + id);
    }

    public void finishReload(Player player) {
        if (!isReloading()) return;
        currentState.setReloading(false);
        ModuleManager.get().triggerReloadFinish(player, this);
        LoggerUtil.debug("[Weapon] Reload finished by " + player.getName() + " for " + id);
    }

    // =====================================================
    // Shooting
    // =====================================================
    public boolean canShoot(Player player) {
        return !isReloading() && !currentState.isShooting();
    }

    public void shoot(Player player) {
        if (!canShoot(player)) {
            LoggerUtil.debug("[Weapon] Cannot shoot: " + player.getName() + " -> " + id);
            return;
        }

        currentState.setShooting(true);

        // モジュールイベント呼び出し
        ModuleManager.get().triggerShoot(player, this);

        // 弾薬・熱量・ジャム処理
        if (ammoSystem) consumeAmmo(player);
        if (heatSystem) addHeat();

        currentState.setShooting(false);
        LoggerUtil.debug("[Weapon] Shot fired by " + player.getName() + " -> " + id);
    }

    // =====================================================
    // Attachment
    // =====================================================
    public void addAttachment(Attachment attachment) {
        if (attachment == null || attachments.contains(attachment)) return;
        attachments.add(attachment);
        attachment.applyToWeapon(this);
    }

    public void removeAttachment(Attachment attachment) {
        if (attachment == null || !attachments.remove(attachment)) return;
        attachment.removeFromWeapon(this);
    }
// =====================================================
// WeaponSaver / WeaponFactory 互換メソッド
// =====================================================
    /**
     * attachment ID を追加
     */
    public void addAttachmentId(String id) {
        // 仮に AttachmentManager から取得する想定
        Attachment a = ModuleManager.get().getAttachmentById(id);
        if (a != null) addAttachment(a);
    }

    /**
     * スコープ有効設定
     */
    public void setScopeEnabled(boolean enabled) {
        setScope(enabled);
    }

    /**
     * スコープズーム設定
     */
    public void setScopeZoom(float fov) {
        setScopeZoomFOV(fov);
    }

    public boolean isZooming(Player player) {
        return zoomingPlayers.getOrDefault(player, false);
    }

    public void setZooming(Player player, boolean zooming) {
        zoomingPlayers.put(player, zooming);
    }

    /**
     * ADS速度設定
     */
    public void setAdsSpeed(float speed) {
        if (stats != null) stats.setAdsSpeed(speed);
    }

    public AttachmentStats getAttachmentStats() {
        AttachmentStats result = AttachmentStats.DEFAULT;
        for (Attachment a : attachments) {
            AttachmentStats s = a.getStats();
            result = new AttachmentStats(
                    result.getRecoilMultiplier() * s.getRecoilMultiplier(),
                    result.getDamageMultiplier() * s.getDamageMultiplier(),
                    result.getFireRateMultiplier() * s.getFireRateMultiplier(),
                    result.getAccuracyMultiplier() * s.getAccuracyMultiplier()
            );
        }
        return result;
    }
}
