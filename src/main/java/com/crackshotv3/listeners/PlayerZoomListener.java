package com.crackshotv3.listeners;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerZoomListener implements Listener {

    private final WeaponAPI weaponAPI;

    public PlayerZoomListener(WeaponAPI weaponAPI) {
        this.weaponAPI = weaponAPI;
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Weapon weapon = weaponAPI.getEquippedWeapon(player);
        if (weapon == null || !weapon.hasScope()) return;

        // ADS開始/終了
        weapon.setZooming(player, event.isSneaking());
    }
}
