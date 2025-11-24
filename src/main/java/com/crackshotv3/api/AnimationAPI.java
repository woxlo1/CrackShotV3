package com.crackshotv3.api;

import com.crackshotv3.core.animation.AnimationEngine;
import com.crackshotv3.core.animation.FirstPersonAnimation;
import org.bukkit.entity.Player;

public class AnimationAPI {

    private final AnimationEngine engine;

    public AnimationAPI(AnimationEngine engine) {
        this.engine = engine;
    }

    /**
     * プレイヤーに一人称アニメを再生
     */
    public void playFirstPersonAnimation(Player player, FirstPersonAnimation animation) {
        engine.playFirstPerson(player, animation);
    }
}
