package com.crackshotv3.core.animation;

import org.bukkit.util.Vector;

/**
 * 一人称武器アニメーションユーティリティ
 */
public final class FirstPersonAnimation {

    private final AnimationSequence sequence;

    private FirstPersonAnimation(AnimationSequence sequence) {
        this.sequence = sequence;
    }

    /**
     * リコイル用アニメーションを作成
     */
    public static FirstPersonAnimation createRecoil(Vector recoilOffset, Vector recoilRotation, long durationTicks) {
        AnimationSequence seq = new AnimationSequence("FP_Recoil");

        // 開始位置
        seq.addKeyframe(0, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));
        // 最大リコイル
        seq.addKeyframe(durationTicks / 2, new AnimationSequence.Transform(recoilOffset, recoilRotation, new Vector(1,1,1)));
        // 元に戻る
        seq.addKeyframe(durationTicks, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));

        return new FirstPersonAnimation(seq);
    }

    /**
     * 発射アニメーション
     */
    public static FirstPersonAnimation createFire(Vector fireOffset, Vector fireRotation, long durationTicks) {
        AnimationSequence seq = new AnimationSequence("FP_Fire");

        seq.addKeyframe(0, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));
        seq.addKeyframe(durationTicks / 2, new AnimationSequence.Transform(fireOffset, fireRotation, new Vector(1,1,1)));
        seq.addKeyframe(durationTicks, new AnimationSequence.Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1)));

        return new FirstPersonAnimation(seq);
    }

    /**
     * AnimationSequence に変換
     */
    public AnimationSequence toSequence() {
        return sequence;
    }
}
