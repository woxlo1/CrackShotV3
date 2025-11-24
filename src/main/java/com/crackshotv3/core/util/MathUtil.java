package com.crackshotv3.core.util;

public class MathUtil {

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static double random(double min, double max) {
        return min + Math.random() * (max - min);
    }
}
