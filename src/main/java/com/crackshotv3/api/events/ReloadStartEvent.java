package com.crackshotv3.api.events;

import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReloadStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Weapon weapon;

    public ReloadStartEvent(Player player, Weapon weapon) {
        this.player = player;
        this.weapon = weapon;
    }

    public Player getPlayer() { return player; }
    public Weapon getWeapon() { return weapon; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
