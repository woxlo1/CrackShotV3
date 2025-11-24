package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;

import static com.crackshotv3.core.util.MessageUtil.send;

/**
 * 物理アイテム消費による弾薬管理。
 */
public class AmmoModule extends Module {

    public AmmoModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "AmmoModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        if (!weapon.hasAmmoSystem()) return;

        if (!weapon.consumeAmmo(player)) {
            send(player, "§c弾薬がありません！");
            debug("Ammo empty for " + weapon.getId());
        }
    }
}
