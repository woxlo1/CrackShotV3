package com.crackshotv3.listeners;

import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final WeaponAPI weaponAPI;

    public PlayerInteractListener(WeaponAPI weaponAPI) {
        this.weaponAPI = weaponAPI;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Weapon weapon = weaponAPI.getEquippedWeapon(player);
        if (weapon == null) return;

        // 右クリックでリロード
        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            weapon.startReload(player);
        }

        // 左クリックで射撃
        if (event.getAction().toString().contains("LEFT_CLICK")) {
            weaponAPI.fireWeapon(player, weapon);
        }
    }
}
