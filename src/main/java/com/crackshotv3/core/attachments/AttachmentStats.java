package com.crackshotv3.core.attachments;

/**
 * アタッチメントが武器に与える補正値
 */
public final class AttachmentStats {

    private final double recoilMultiplier;
    private final double damageMultiplier;
    private final double fireRateMultiplier;
    private final double accuracyMultiplier;

    public AttachmentStats(double recoilMultiplier, double damageMultiplier,
                           double fireRateMultiplier, double accuracyMultiplier) {
        this.recoilMultiplier = recoilMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.fireRateMultiplier = fireRateMultiplier;
        this.accuracyMultiplier = accuracyMultiplier;
    }

    public double getRecoilMultiplier() { return recoilMultiplier; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public double getFireRateMultiplier() { return fireRateMultiplier; }
    public double getAccuracyMultiplier() { return accuracyMultiplier; }

    public static final AttachmentStats DEFAULT = new AttachmentStats(1.0, 1.0, 1.0, 1.0);
}
