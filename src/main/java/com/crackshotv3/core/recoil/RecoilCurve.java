package com.crackshotv3.core.recoil;

/**
 * リコイル回復や強度に使える曲線ユーティリティ
 *
 * - evaluate(t) は 0..1 の入力に対し 0..1 を返す
 * - 内蔵イージング: linear, easeOutQuad, exponentialDecay をサポート
 *
 * 例: new RecoilCurve(RecoilCurve.Type.EASE_OUT_QUAD)
 */
public final class RecoilCurve {

    public enum Type {
        LINEAR,
        EASE_OUT_QUAD,
        EXPONENTIAL_DECAY
    }

    private final Type type;
    private final double decayRate; // EXPONENTIAL_DECAY 用（大きいほど早く下がる）

    public RecoilCurve(Type type) {
        this(type, 5.0);
    }

    public RecoilCurve(Type type, double decayRate) {
        this.type = type;
        this.decayRate = decayRate;
    }

    /**
     * t in [0,1] -> value in [0,1]
     */
    public double evaluate(double t) {
        t = clamp01(t);
        switch (type) {
            case LINEAR:
                return t;
            case EASE_OUT_QUAD:
                return 1.0 - (1.0 - t) * (1.0 - t);
            case EXPONENTIAL_DECAY:
                // 1 - exp(-k * t) scaled to [0,1]
                double k = decayRate;
                double v = 1.0 - Math.exp(-k * t);
                // normalize so t=1 -> 1
                double norm = 1.0 - Math.exp(-k * 1.0);
                if (norm == 0) return v;
                return v / norm;
            default:
                return t;
        }
    }

    private static double clamp01(double v) {
        if (v <= 0.0) return 0.0;
        if (v >= 1.0) return 1.0;
        return v;
    }
}
