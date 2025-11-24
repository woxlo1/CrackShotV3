package com.crackshotv3.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class EventAPI {

    /**
     * 任意の Bukkit イベントを安全に呼び出す
     */
    public <T extends Event> void callEvent(T event) {
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("CrackShotV3"), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
