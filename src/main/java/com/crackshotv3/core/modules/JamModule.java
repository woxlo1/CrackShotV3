package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import org.bukkit.entity.Player;

import java.util.Random;

import static com.crackshotv3.core.util.MessageUtil.send;

/**
 * 発射時のジャム（故障）確率処理。
 */
public class JamModule extends Module {

    private final Random random = new Random();

    public JamModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "JamModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        if (!weapon.hasJamSystem()) return;

        if (random.nextDouble() <= weapon.getJamChance()) {
            weapon.setJammed(true);
            send(player, "§c武器がジャム故障しました！");
            debug("Weapon jammed: " + weapon.getId());
        }
    }
}
