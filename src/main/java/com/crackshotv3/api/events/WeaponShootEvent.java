package com.crackshotv3.api.events;

import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WeaponShootEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player shooter;
    private final Weapon weapon;

    public WeaponShootEvent(Player shooter, Weapon weapon) {
        this.shooter = shooter;
        this.weapon = weapon;
    }

    public Player getShooter() { return shooter; }
    public Weapon getWeapon() { return weapon; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
