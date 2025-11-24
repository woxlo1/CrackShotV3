package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponState;
import org.bukkit.entity.Player;

/**
 * リロード処理を担当。
 */
public class ReloadModule extends Module {

    public ReloadModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "ReloadModule";
    }

    @Override
    public void onReload(Player player, Weapon weapon) {
        if (weapon.isReloading()) return;  // Weapon クラス経由で呼ぶ

        weapon.startReload(player);
        debug("Reload started for weapon " + weapon.getId());
    }
}
