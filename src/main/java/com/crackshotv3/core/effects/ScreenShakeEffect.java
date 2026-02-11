package com.crackshotv3.core.effects;

import com.crackshotv3.CrackShotV3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 画面揺れ（ADSやリコイル連動）
 *
 * 修正点:
 * 以前は play() 内で for ループを回して毎回 teleport() を呼んでいたため、
 * 1tick に複数回テレポートが走りサーバーが詰まる問題があった。
 * BukkitScheduler で 1tick ごとに1回だけテレポートするよう変更。
 */
public class ScreenShakeEffect implements Effect {

    private final float intensity;
    private final int duration; // tick数

    public ScreenShakeEffect(float intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
    }

    @Override
    public void play(Player player) {
        Location base = player.getLocation().clone();

        // 修正: 各tick で1回ずつテレポート
        for (int i = 0; i < duration; i++) {
            final int tick = i;
            Bukkit.getScheduler().runTaskLater(CrackShotV3.getInstance(), () -> {
                if (!player.isOnline()) return;

                float offsetX = (float) ((Math.random() - 0.5) * intensity);
                float offsetY = (float) ((Math.random() - 0.5) * intensity);
                float offsetZ = (float) ((Math.random() - 0.5) * intensity);

                // 位置ではなく向き(pitch/yaw)をずらす方が自然なスクリーンシェイクになる
                Location shakeLoc = player.getLocation().clone();
                shakeLoc.setPitch(shakeLoc.getPitch() + offsetY * 2);
                shakeLoc.setYaw(shakeLoc.getYaw() + offsetX * 2);
                player.teleport(shakeLoc);
            }, tick);
        }
    }

    @Override
    public void play(Location location) {
        // 個別ロケーションでは無効
    }
}