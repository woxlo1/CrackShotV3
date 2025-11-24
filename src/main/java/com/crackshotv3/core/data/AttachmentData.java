package com.crackshotv3.core.data;

/**
 * アタッチメントの静的データ
 */
public class AttachmentData {

    private final String id;
    private String displayName;
    private double damageMultiplier;
    private double recoilMultiplier;
    private double fireRateMultiplier;
    private double accuracyMultiplier;

    public AttachmentData(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.damageMultiplier = 1.0;
        this.recoilMultiplier = 1.0;
        this.fireRateMultiplier = 1.0;
        this.accuracyMultiplier = 1.0;
    }

    // ================================
    // Getter / Setter
    // ================================
    public String getId() { return id; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public double getDamageMultiplier() { return damageMultiplier; }
    public void setDamageMultiplier(double damageMultiplier) { this.damageMultiplier = damageMultiplier; }

    public double getRecoilMultiplier() { return recoilMultiplier; }
    public void setRecoilMultiplier(double recoilMultiplier) { this.recoilMultiplier = recoilMultiplier; }

    public double getFireRateMultiplier() { return fireRateMultiplier; }
    public void setFireRateMultiplier(double fireRateMultiplier) { this.fireRateMultiplier = fireRateMultiplier; }

    public double getAccuracyMultiplier() { return accuracyMultiplier; }
    public void setAccuracyMultiplier(double accuracyMultiplier) { this.accuracyMultiplier = accuracyMultiplier; }
}
