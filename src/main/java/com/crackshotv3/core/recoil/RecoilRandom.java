package com.crackshotv3.core.recoil;

import java.security.SecureRandom;

/**
 * リコイルに微小なランダム性を付与するユーティリティ
 */
public final class RecoilRandom {

    private static final SecureRandom RNG = new SecureRandom();

    private RecoilRandom() {}

    /**
     * -yaw..+yaw, -pitch..+pitch の範囲でランダムオフセットを返す
     *
     * @param maxPitchDeg 最大ピッチ差（度）
     * @param maxYawDeg   最大ヨー差（度）
     */
    public static RecoilPattern.RecoilOffset randomOffset(double maxPitchDeg, double maxYawDeg) {
        double p = (RNG.nextDouble() * 2.0 - 1.0) * maxPitchDeg;
        double y = (RNG.nextDouble() * 2.0 - 1.0) * maxYawDeg;
        return new RecoilPattern.RecoilOffset(p, y);
    }
}
