package com.crackshotv3.core.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public final class MessageUtil {

    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.builder().character('&').hexCharacter('#').build();

    private static String PREFIX = "§7[§cCrackShotV3§7] ";

    private MessageUtil() {}

    // ==========================
    //   PREFIX 設定
    // ==========================
    public static void setPrefix(String prefix) {
        PREFIX = color(prefix + " ");
    }

    // ==========================
    //   色コード変換
    // ==========================
    public static String color(String msg) {
        if (msg == null) return "";
        return LEGACY.serialize(LEGACY.deserialize(msg));
    }

    // ==========================
    //   Placeholder 置換
    // ==========================
    public static String applyPlaceholders(String message, Map<String, String> placeholders) {
        if (message == null) return "";
        if (placeholders == null || placeholders.isEmpty()) return message;

        String result = message;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            result = result.replace("{" + e.getKey() + "}", e.getValue());
        }
        return result;
    }

    // ==========================
    //   メッセージ送信（text）
    // ==========================
    public static void send(CommandSender sender, String msg) {
        if (sender == null || msg == null) return;
        sender.sendMessage(PREFIX + color(msg));
    }

    public static void send(Player player, String msg) {
        if (player == null || msg == null) return;
        player.sendMessage(PREFIX + color(msg));
    }

    public static void broadcast(String msg) {
        if (msg == null) return;
        Bukkit.broadcastMessage(PREFIX + color(msg));
    }

    // ==========================
    //   Adventure 対応
    // ==========================
    public static void sendMini(CommandSender sender, String miniMsg) {
        if (sender == null || miniMsg == null) return;
        Component comp = MINI.deserialize(miniMsg);

        // CommandSender を Audience にキャストして送信
        Audience audience = (Audience) sender;
        audience.sendMessage(comp);
    }

    public static Component toComponent(String msg) {
        if (msg == null) return Component.empty();
        return LEGACY.deserialize(color(msg));
    }
}
