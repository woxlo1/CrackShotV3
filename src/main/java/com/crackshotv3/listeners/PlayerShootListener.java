package com.crackshotv3.listeners;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.api.ProjectileAPI;
import com.crackshotv3.core.projectile.ProjectileBase;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerShootListener implements Listener {

    private final WeaponAPI weaponAPI;
    private final ProjectileAPI projectileAPI;

    public PlayerShootListener(WeaponAPI weaponAPI, ProjectileAPI projectileAPI) {
        this.weaponAPI = weaponAPI;
        this.projectileAPI = projectileAPI;
    }

    @EventHandler
    public void onPlayerShoot(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Weapon weapon = weaponAPI.getEquippedWeapon(player); // API経由で取得
        if (weapon == null) return;

        // 射撃処理
        ProjectileBase projectile = projectileAPI.spawnProjectile(player, weapon); // API経由
        if (projectile != null) {
            projectile.onTick(); // 初期Tick処理
        }

        // 弾薬消費・発射音などは Weapon クラス内で処理
        weapon.shoot(player);
    }
}
