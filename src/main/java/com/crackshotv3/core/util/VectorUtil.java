package com.crackshotv3.core.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class VectorUtil {

    public static Vector randomSpread(Vector direction, double spread) {
        // spread = ±角度 deviation
        double x = (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
        double y = (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
        double z = (ThreadLocalRandom.current().nextDouble() - 0.5) * spread;
        return direction.clone().add(new Vector(x, y, z)).normalize();
    }

    public static Vector fromLocation(Location loc) {
        return loc.getDirection().normalize();
    }

    public static Vector knockback(Vector base, double power) {
        return base.clone().normalize().multiply(power);
    }

    public static double distanceSquared(Location a, Location b) {
        return a.toVector().distanceSquared(b.toVector());
    }
}
