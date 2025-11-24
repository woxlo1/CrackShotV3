package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * バースト射撃（3点/5点など）。
 */
public class BurstModule extends Module {

    public BurstModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "BurstModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        int burst = weapon.getBurstCount();
        long interval = weapon.getBurstInterval();

        if (burst <= 1) return;

        new BukkitRunnable() {
            int shots = 0;

            @Override
            public void run() {
                if (shots >= burst) {
                    cancel();
                    return;
                }

                if (weapon.isJammed() || weapon.isOverheated() || !weapon.consumeAmmo(player)) {
                    cancel();
                    return;
                }

                weapon.shoot(player);
                shots++;
                debug("Burst " + shots + "/" + burst);
            }
        }.runTaskTimer(plugin, 0L, interval);
    }
}
