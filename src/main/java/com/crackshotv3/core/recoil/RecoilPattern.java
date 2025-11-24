package com.crackshotv3.core.recoil;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * リコイルパターン（ショットごとのピッチ/ヨーオフセットを定義）
 *
 * 内部は List<RecoilOffset> を保持。Y軸（yaw）は右正、X軸（pitch）は上向き正（Minecraft の pitch と符号揃え）。
 *
 * 例（YAML）:
 * pattern:
 *  - { pitch: -1.5, yaw: 0.2 }
 *  - { pitch: -1.6, yaw: 0.3 }
 */
public final class RecoilPattern {

    private final List<RecoilOffset> offsets;

    public RecoilPattern() {
        this.offsets = new ArrayList<>();
    }

    public RecoilPattern(List<RecoilOffset> offsets) {
        this.offsets = new ArrayList<>(Objects.requireNonNull(offsets));
    }

    public void addOffset(double pitchDeg, double yawDeg) {
        offsets.add(new RecoilOffset(pitchDeg, yawDeg));
    }

    public int size() { return offsets.size(); }

    /**
     * 指定 shotIndex のオフセットを返す（範囲外は最後の要素を返す）
     */
    public RecoilOffset getOffset(int shotIndex) {
        if (offsets.isEmpty()) return RecoilOffset.ZERO;
        if (shotIndex < 0) return offsets.get(0);
        if (shotIndex >= offsets.size()) return offsets.get(offsets.size() - 1);
        return offsets.get(shotIndex);
    }

    public List<RecoilOffset> getOffsets() { return new ArrayList<>(offsets); }

    // ======= 内部型 =======
    public static final class RecoilOffset {
        public static final RecoilOffset ZERO = new RecoilOffset(0, 0);
        private final double pitch;
        private final double yaw;

        public RecoilOffset(double pitch, double yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }

        public double getPitch() { return pitch; }
        public double getYaw() { return yaw; }

        @Override
        public String toString() {
            return "RecoilOffset{pitch=" + pitch + ", yaw=" + yaw + '}';
        }
    }
}
