package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 武器効果の基本インターフェース
 */
public interface Effect {
    void play(Player player);
    void play(Location location);
}
