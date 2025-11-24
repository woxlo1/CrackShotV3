package com.crackshotv3.gui;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.gui.base.BaseGUI;
import org.bukkit.entity.Player;

import java.util.function.DoubleConsumer;

public class NumericInputGUI extends BaseGUI {

    private final String title;
    private final double initial;
    private final double min, max;
    private final DoubleConsumer callback;

    public NumericInputGUI(CrackShotV3 plugin, Player player, String title, double initial, double min, double max, DoubleConsumer callback) {
        super(plugin, player);
        this.title = title;
        this.initial = initial;
        this.min = min;
        this.max = max;
        this.callback = callback;
    }

    @Override
    public String getTitle() { return "§6Input: " + title; }

    @Override
    public int getSize() { return 27; }

    @Override
    public void draw() {
        fillBackground();
        setItem(11, item(org.bukkit.Material.PAPER, "§aCurrent: " + initial, "§7クリックで +1"), ev -> {
            double v = clamp(initial + 1, min, max);
            callback.accept(v);
            player.closeInventory();
        });
        setItem(13, item(org.bukkit.Material.WRITABLE_BOOK, "§eManual entry disabled", "§7Use +1 / -1 / +10"), ev -> {});
        setItem(15, item(org.bukkit.Material.PAPER, "§aCurrent: " + initial, "§7クリックで -1"), ev -> {
            double v = clamp(initial - 1, min, max);
            callback.accept(v);
            player.closeInventory();
        });

        setItem(22, item(org.bukkit.Material.EMERALD, "§aAccept", "§7Accept value"), ev -> {
            callback.accept(initial);
            player.closeInventory();
        });

        addBackButton(26, () -> player.closeInventory());
    }

    private double clamp(double v, double a, double b) {
        return Math.max(a, Math.min(b, v));
    }
}
