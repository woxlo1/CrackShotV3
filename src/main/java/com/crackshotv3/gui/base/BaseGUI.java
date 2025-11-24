package com.crackshotv3.gui.base;

import com.crackshotv3.CrackShotV3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * GUI の基底クラス
 * 1.20.4 対応
 */
public abstract class BaseGUI implements Listener {

    protected final CrackShotV3 plugin;
    protected final Player player;
    protected Inventory inv;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();

    public BaseGUI(CrackShotV3 plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public abstract String getTitle();
    public abstract int getSize();
    public abstract void draw();

    /**
     * 背景を灰色ガラスで埋める
     */
    protected void fillBackground() {
        inv = Bukkit.createInventory(player, getSize(), getTitle());
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) meta.setDisplayName(" ");
        glass.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }
    }

    /**
     * アイテム生成ヘルパー
     */
    protected ItemStack item(Material mat, String name, String... lore) {
        ItemStack is = new ItemStack(mat, 1);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> loreList = new ArrayList<>();
            for (String s : lore) loreList.add(s);
            meta.setLore(loreList);
            is.setItemMeta(meta);
        }
        return is;
    }

    protected void setItem(int slot, ItemStack is) {
        inv.setItem(slot, is);
    }

    protected void setItem(int slot, ItemStack is, Consumer<InventoryClickEvent> onClick) {
        inv.setItem(slot, is);
        actions.put(slot, onClick);
    }

    protected void addBackButton(int slot, Runnable onBack) {
        setItem(slot, item(Material.ARROW, "§cBack"), ev -> onBack.run());
    }

    /**
     * GUI を開く
     */
    public void open() {
        draw();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        player.openInventory(inv);
    }

    /**
     * GUI を再描画
     */
    public void redraw() {
        if (player.getOpenInventory() != null
                && player.getOpenInventory().getTitle().equals(getTitle())) {
            draw();
            player.openInventory(inv);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getInventory().equals(inv)) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            Consumer<InventoryClickEvent> act = actions.get(slot);
            if (act != null) act.accept(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player && e.getInventory().equals(inv)) {
            HandlerList.unregisterAll(this);
        }
    }

    // 必要に応じてオーバーライド
    public void handleClick(InventoryClickEvent e) {}
}
