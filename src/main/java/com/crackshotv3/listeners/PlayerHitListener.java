package com.crackshotv3.listeners;

import com.crackshotv3.api.ProjectileAPI;
import com.crackshotv3.core.projectile.ProjectileBase;
import com.crackshotv3.core.projectile.ProjectileEngine;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * PlayerHitListener
 *
 * 修正点:
 * ProjectileBase は Bukkit エンティティではないため、
 * EntityDamageByEntityEvent の damager として instanceof チェックでマッチしない。
 *
 * 実際のヒット判定は各 ProjectileBase サブクラス (PhysicalProjectile 等) の
 * onTick() 内で getNearbyEntities() を使って自前でダメージを与えているため、
 * このリスナーは「バニラの矢など他のProjectile経由のダメージを遮断しない」
 * 補助的な役割に留める。
 *
 * ProjectileBase からのダメージは ShootingModule / PhysicalProjectile 等が
 * entity.damage(amount, shooter) で直接発行するため、
 * ここでは EntityDamageByEntityEvent で shooter が Player かつ
 * アクティブ弾丸と一致する場合のみヒット登録を行う。
 */
public class PlayerHitListener implements Listener {

    private final ProjectileAPI projectileAPI;

    public PlayerHitListener(ProjectileAPI projectileAPI) {
        this.projectileAPI = projectileAPI;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // damager がプレイヤーでない場合はスキップ
        // (PhysicalProjectile 等は entity.damage(amount, shooterPlayer) を呼ぶため
        //  damager は Player になる)
        if (!(event.getDamager() instanceof Player shooter)) return;

        // 被ダメージ対象がプレイヤーでなければスキップ
        if (!(event.getEntity() instanceof Player target)) return;

        // 射手がアクティブな弾丸を持っているか確認
        // (弾丸の onTick 内でダメージが与えられた直後にこのイベントが発火する)
        boolean hasActiveProjectile = ProjectileEngine.getActiveProjectiles()
                .stream()
                .anyMatch(p -> p.getShooter() != null && p.getShooter().equals(shooter));

        if (!hasActiveProjectile) return;

        double damage = event.getFinalDamage();

        // ヒット登録
        projectileAPI.registerHit(shooter, target, damage);
    }
}