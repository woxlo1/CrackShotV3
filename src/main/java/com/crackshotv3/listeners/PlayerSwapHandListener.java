package com.crackshotv3.listeners;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandListener implements Listener {

    private final WeaponAPI weaponAPI;

    public PlayerSwapHandListener(WeaponAPI weaponAPI) {
        this.weaponAPI = weaponAPI;
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        // メインハンドのアイテムから武器を取得
        Weapon weapon = weaponAPI.getEquippedWeapon(player);
        if (weapon == null) return;

        // 装備中武器を更新（WeaponAPI側で equipWeapon を実装している場合）
        weaponAPI.equipWeapon(player, weapon);
    }
}
