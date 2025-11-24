package com.crackshotv3.api.events;

import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScopeToggleEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Weapon weapon;
    private final boolean enabled;

    public ScopeToggleEvent(Player player, Weapon weapon, boolean enabled) {
        this.player = player;
        this.weapon = weapon;
        this.enabled = enabled;
    }

    public Player getPlayer() { return player; }
    public Weapon getWeapon() { return weapon; }
    public boolean isEnabled() { return enabled; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
