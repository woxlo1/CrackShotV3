package com.crackshotv3.api.events;

import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WeaponHitEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player shooter;
    private final Weapon weapon;
    private final Entity hitEntity;

    public WeaponHitEvent(Player shooter, Weapon weapon, Entity hitEntity) {
        this.shooter = shooter;
        this.weapon = weapon;
        this.hitEntity = hitEntity;
    }

    public Player getShooter() { return shooter; }
    public Weapon getWeapon() { return weapon; }
    public Entity getHitEntity() { return hitEntity; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
