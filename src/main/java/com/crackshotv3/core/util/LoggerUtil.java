package com.crackshotv3.core.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerUtil {

    private static Logger logger;
    private static String pluginName = "CrackShotV3";
    private static boolean debugEnabled = false;

    private LoggerUtil() {}

    // ==========================
    //   初期化（メインクラスで実行）
    // ==========================
    public static void init(Plugin plugin, boolean enableDebug) {
        logger = plugin.getLogger();
        pluginName = plugin.getName();
        debugEnabled = enableDebug;
        info("Logger initialized. Debug=" + enableDebug);
    }

    // ==========================
    //   基本ログ
    // ==========================
    public static void info(String msg) {
        if (logger != null) logger.log(Level.INFO, format(msg));
    }

    public static void warn(String msg) {
        if (logger != null) logger.log(Level.WARNING, format(msg));
    }

    public static void error(String msg) {
        if (logger != null) logger.log(Level.SEVERE, format(msg));
    }

    public static void error(String msg, Throwable throwable) {
        if (logger != null) logger.log(Level.SEVERE, format(msg), throwable);
    }

    // ==========================
    //   デバッグ
    // ==========================
    public static void debug(String msg) {
        if (!debugEnabled || logger == null) return;
        logger.log(Level.INFO, "[DEBUG] " + format(msg));
    }

    // ==========================
    //   コンソールに色付き出力
    // ==========================
    public static void console(String coloredMsg) {
        Bukkit.getConsoleSender().sendMessage(coloredMsg);
    }

    // ==========================
    //   共通フォーマット
    // ==========================
    private static String format(String msg) {
        return "[" + pluginName + "] " + msg;
    }
}
