package com.crackshotv3.core.animation;

import org.bukkit.util.Vector;

/**
 * 三人称武器アニメーションユーティリティ
 */
public final class ThirdPersonAnimation {

    private ThirdPersonAnimation() {}

    /**
     * 発射時のマズルフラッシュ用アニメ
     */
    public static AnimationSequence createMuzzleFlashSequence(Vector offset, long durationTicks) {
        AnimationSequence seq = new AnimationSequence("TP_MuzzleFlash");

        seq.addKeyframe(0, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks / 2, new AnimationSequence.Transform(offset, new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));

        return seq;
    }

    /**
     * 発射時の反動アニメ（後方に少し下がる動き）
     */
    public static AnimationSequence createRecoilSequence(Vector recoilOffset, long durationTicks) {
        AnimationSequence seq = new AnimationSequence("TP_Recoil");

        seq.addKeyframe(0, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks / 2, new AnimationSequence.Transform(recoilOffset, new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));

        return seq;
    }

    /**
     * リロード用の移動アニメーション
     */
    public static AnimationSequence createReloadSequence(Vector reloadOffset, long durationTicks) {
        AnimationSequence seq = new AnimationSequence("TP_Reload");

        seq.addKeyframe(0, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks / 2, new AnimationSequence.Transform(reloadOffset, new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));

        return seq;
    }
}
