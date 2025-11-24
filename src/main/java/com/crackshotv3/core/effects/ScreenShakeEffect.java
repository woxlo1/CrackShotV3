package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 画面揺れ（ADSやリコイル連動）
 */
public class ScreenShakeEffect implements Effect {

    private final float intensity;  // 揺れの大きさ
    private final int duration;     // tick単位

    public ScreenShakeEffect(float intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
    }

    @Override
    public void play(Player player) {
        // プレイヤーの位置を少し振動させるだけ（純粋なスクリーン揺れはBukkitでは不可）
        Location loc = player.getLocation();
        for (int i = 0; i < duration; i++) {
            float offsetX = (float) ((Math.random() - 0.5) * intensity);
            float offsetY = (float) ((Math.random() - 0.5) * intensity);
            float offsetZ = (float) ((Math.random() - 0.5) * intensity);
            player.teleport(loc.clone().add(offsetX, offsetY, offsetZ));
        }
    }

    @Override
    public void play(Location location) {
        // 個別ロケーションでは無効
    }
}
