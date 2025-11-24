package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;

/**
 * スコープズーム管理。
 */
public class ScopeModule extends Module {

    public ScopeModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "ScopeModule";
    }

    @Override
    public void onTick(Player player, Weapon weapon) {
        if (!weapon.hasScope()) return;

        if (player.isSneaking()) {
            float fov = (float) weapon.getScopeZoomFOV();
            player.setWalkSpeed(0.2f * (1f / fov));
            debug("Zoom active: FOV=" + fov);
        } else {
            player.setWalkSpeed(0.2f); // default
        }
    }
}
