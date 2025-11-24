package com.crackshotv3.core.data;

import org.bukkit.util.Vector;

/**
 * 武器・弾丸のアニメーション情報
 */
public class AnimationData {

    private final String id;
    private Vector kickBack;      // 反動
    private Vector sway;          // 銃の揺れ
    private int duration;         // tick単位

    public AnimationData(String id) {
        this.id = id;
        this.kickBack = new Vector(0, 0, 0);
        this.sway = new Vector(0, 0, 0);
        this.duration = 5;
    }

    // ================================
    // Getter / Setter
    // ================================
    public String getId() { return id; }

    public Vector getKickBack() { return kickBack; }
    public void setKickBack(Vector kickBack) { this.kickBack = kickBack; }

    public Vector getSway() { return sway; }
    public void setSway(Vector sway) { this.sway = sway; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
