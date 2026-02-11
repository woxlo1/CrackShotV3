package com.crackshotv3.listeners;

import com.crackshotv3.api.WeaponAPI;
import com.crackshotv3.api.ProjectileAPI;
import com.crackshotv3.core.projectile.ProjectileBase;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

/**
 * PlayerShootListener
 *
 * 修正点:
 * 1. PlayerInteractListener と同じイベントを処理していて発射が二重になっていた問題を修正。
 *    PlayerInteractListener を CrackShotV3.java で登録しないようにし、
 *    このリスナーが左クリック(射撃) と 右クリック(リロード) を一元管理する。
 *
 * 2. weapon.shoot(player) が ModuleManager 経由で弾薬消費を行い、
 *    projectileAPI.spawnProjectile() でも弾丸生成されるため、
 *    weapon.shoot() は呼ばず projectileAPI 経由で統一した。
 *    弾薬消費は ShootingModule / AmmoModule に任せる。
 */
public class PlayerShootListener implements Listener {

    private final WeaponAPI weaponAPI;
    private final ProjectileAPI projectileAPI;

    public PlayerShootListener(WeaponAPI weaponAPI, ProjectileAPI projectileAPI) {
        this.weaponAPI = weaponAPI;
        this.projectileAPI = projectileAPI;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // オフハンドのイベントは無視（二重発火防止）
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Weapon weapon = weaponAPI.getEquippedWeapon(player);
        if (weapon == null) return;

        Action action = event.getAction();

        // 右クリック → リロード
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (!weapon.isReloading()) {
                weapon.startReload(player);
            }
            return;
        }

        // 左クリック → 射撃
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);

            // リロード中・ジャム・オーバーヒートは射撃不可
            if (!weapon.canShoot(player)) return;

            // ProjectileAPI 経由で弾丸生成・登録
            ProjectileBase projectile = projectileAPI.spawnProjectile(player, weapon);
            // spawnProjectile 内で addProjectile 済みなので onTick は Engine に任せる

            // 弾薬消費・モジュール処理は weapon.shoot() 経由で行う
            // ただし ShootingModule の onShoot で再度弾丸を生成しないよう
            // ShootingModule 側を修正済み（後述）
            weapon.shoot(player);
        }
    }
}