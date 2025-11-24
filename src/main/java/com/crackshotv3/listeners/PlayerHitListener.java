package com.crackshotv3.listeners;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.api.ProjectileAPI;
import com.crackshotv3.core.projectile.ProjectileBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitListener implements Listener {

    private final ProjectileAPI projectileAPI;

    public PlayerHitListener(ProjectileAPI projectileAPI) {
        this.projectileAPI = projectileAPI;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof ProjectileBase projectile)) return;

        Entity target = event.getEntity();
        double damage = projectile.getModifiedDamage();
        event.setDamage(damage);

        if (target instanceof Player p) {
            // プレイヤーへのヒット登録（ProjectileAPI 内部で管理）
            projectileAPI.registerHit(projectile.getShooter(), p, damage);
        }

        projectile.expire();
    }
}
