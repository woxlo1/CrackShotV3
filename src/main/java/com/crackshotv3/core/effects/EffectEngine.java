package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 全効果の統括管理
 */
public class EffectEngine {

    private final List<Effect> effects = new ArrayList<>();

    public void registerEffect(Effect effect) {
        if (effect != null && !effects.contains(effect)) effects.add(effect);
    }

    public void playAll(Player player) {
        for (Effect e : effects) e.play(player);
    }

    public void playAll(Location location) {
        for (Effect e : effects) e.play(location);
    }

    public void clearEffects() {
        effects.clear();
    }
}
