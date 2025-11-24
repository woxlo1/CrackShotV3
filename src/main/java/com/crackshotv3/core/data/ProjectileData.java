package com.crackshotv3.core.data;

import org.bukkit.util.Vector;

/**
 * 弾丸の静的データ
 */
public class ProjectileData {

    private final String id;
    private double speed;
    private double damage;
    private boolean explosive;
    private Vector gravity;

    public ProjectileData(String id) {
        this.id = id;
        this.speed = 2.0;
        this.damage = 5.0;
        this.explosive = false;
        this.gravity = new Vector(0, -0.03, 0);
    }

    // ================================
    // Getter / Setter
    // ================================
    public String getId() { return id; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }

    public boolean isExplosive() { return explosive; }
    public void setExplosive(boolean explosive) { this.explosive = explosive; }

    public Vector getGravity() { return gravity; }
    public void setGravity(Vector gravity) { this.gravity = gravity; }
}
