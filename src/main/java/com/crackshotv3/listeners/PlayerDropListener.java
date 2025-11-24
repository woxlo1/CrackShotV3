package com.crackshotv3.listeners;

import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.core.util.MessageUtil;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {

    private final WeaponAPI weaponAPI;

    public PlayerDropListener(WeaponAPI weaponAPI) {
        this.weaponAPI = weaponAPI;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Weapon weapon = weaponAPI.getWeaponFromItem(event.getItemDrop().getItemStack());
        if (weapon != null) {
            event.setCancelled(true);
            MessageUtil.send(event.getPlayer(), "§c武器は落とせません！");
        }
    }
}
