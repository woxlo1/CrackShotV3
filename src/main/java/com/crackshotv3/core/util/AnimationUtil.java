package com.crackshotv3.core.util;

import org.bukkit.entity.Player;

/**
 * AnimationUtil
 *
 * 修正点:
 * ProtocolLib の EntityUseAction をインポートしていたがクラスが存在せず
 * コンパイルエラーになっていた。未使用インポートを全て削除。
 */
public class AnimationUtil {

    /**
     * スコープ FOV の疑似変更（ProtocolLib実装は別途）
     */
    public static void playScopeFOV(Player p, float fov) {
        // ProtocolLib でのパケット操作は別途実装
        // ここでは何もしない（stub）
    }

    public static void swing(Player p) {
        p.swingMainHand();
    }

    public static void playReloadAnimation(Player p) {
        p.swingOffHand();
    }
}