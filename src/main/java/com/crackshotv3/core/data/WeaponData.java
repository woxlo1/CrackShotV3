package com.crackshotv3.core.data;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * 武器の静的データ（ID, 表示名, 材質, 基本ステータス）
 */
public class WeaponData {

    private final String id;
    private String displayName;
    private Material material;
    private double damage;
    private double fireRate;
    private int maxAmmo;
    private boolean hasScope;
    private List<String> attachmentIds;

    public WeaponData(String id, String displayName, Material material) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.damage = 10.0;
        this.fireRate = 1.0;
        this.maxAmmo = 30;
        this.hasScope = false;
        this.attachmentIds = new ArrayList<>();
    }

    // ================================
    // Getter / Setter
    // ================================
    public String getId() { return id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }

    public double getFireRate() { return fireRate; }
    public void setFireRate(double fireRate) { this.fireRate = fireRate; }

    public int getMaxAmmo() { return maxAmmo; }
    public void setMaxAmmo(int maxAmmo) { this.maxAmmo = maxAmmo; }

    public boolean hasScope() { return hasScope; }
    public void setScope(boolean hasScope) { this.hasScope = hasScope; }

    public List<String> getAttachmentIds() { return attachmentIds; }
    public void addAttachmentId(String id) { this.attachmentIds.add(id); }
    public void removeAttachmentId(String id) { this.attachmentIds.remove(id); }
}
