package com.crackshotv3.core.animation;

import org.bukkit.util.Vector;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * アニメーションのキー情報（時間に応じたTransform）
 */
public final class AnimationSequence {

    private final String name;
    private final NavigableMap<Long, Transform> keyframes;

    public AnimationSequence(String name) {
        this.name = name;
        this.keyframes = new TreeMap<>();
    }

    public String getName() { return name; }

    public void addKeyframe(long tick, Transform transform) {
        keyframes.put(tick, transform);
    }

    public Transform getTransformAt(long tick) {
        if (keyframes.isEmpty()) return Transform.IDENTITY;
        Long floor = keyframes.floorKey(tick);
        Long ceil  = keyframes.ceilingKey(tick);
        if (floor == null) return keyframes.get(ceil);
        if (ceil == null)  return keyframes.get(floor);
        if (floor.equals(ceil)) return keyframes.get(floor);

        // 線形補間
        Transform start = keyframes.get(floor);
        Transform end   = keyframes.get(ceil);
        double alpha = (tick - floor) / (double)(ceil - floor);
        return Transform.lerp(start, end, alpha);
    }

    // Transform情報
    public static final class Transform {
        public static final Transform IDENTITY = new Transform(new Vector(0,0,0), new Vector(0,0,0), new Vector(1,1,1));

        private final Vector position;
        private final Vector rotation; // pitch/yaw/roll in degrees
        private final Vector scale;    // スケール

        public Transform(Vector position, Vector rotation, Vector scale) {
            this.position = position;
            this.rotation = rotation;
            this.scale = scale;
        }

        public Vector getPosition() { return position.clone(); }
        public Vector getRotation() { return rotation.clone(); }
        public Vector getScale() { return scale.clone(); }

        public static Transform lerp(Transform a, Transform b, double t) {
            Vector pos = a.position.clone().multiply(1-t).add(b.position.clone().multiply(t));
            Vector rot = a.rotation.clone().multiply(1-t).add(b.rotation.clone().multiply(t));
            Vector scl = a.scale.clone().multiply(1-t).add(b.scale.clone().multiply(t));
            return new Transform(pos, rot, scl);
        }
    }
}
