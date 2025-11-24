package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;

import static com.crackshotv3.core.util.MessageUtil.send;

/**
 * 連射によるオーバーヒート管理。
 */
public class HeatModule extends Module {

    public HeatModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "HeatModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        if (!weapon.hasHeatSystem()) return;

        weapon.addHeat(weapon.getHeatPerShot());

        if (weapon.isOverheated()) {
            send(player, "§c武器がオーバーヒートしました！");
            debug("Weapon overheated: " + weapon.getId());
        }
    }

    @Override
    public void onTick(Player player, Weapon weapon) {
        weapon.coolDownHeat();
    }
}
