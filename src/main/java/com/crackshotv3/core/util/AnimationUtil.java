package com.crackshotv3.core.util;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import org.bukkit.entity.Player;

public class AnimationUtil {

    public static void playScopeFOV(Player p, float fov) {
        try {
            // これは一例: 実際にはクライアント側の FOV スケールに応じて調整
            // 本当に FOV を変更するには Clientbound Entity Metadata Packet などを操作
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swing(Player p) {
        p.swingMainHand();
    }

    public static void playReloadAnimation(Player p) {
        p.swingOffHand();
    }
}
