package com.crackshotv3.core.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import com.crackshotv3.CrackShotV3;

public class NBTUtil {

    private static CrackShotV3 plugin;

    public static void init(CrackShotV3 pl) {
        plugin = pl;
    }

    private static NamespacedKey key(String path) {
        return new NamespacedKey(plugin, path);
    }

    public static ItemStack setString(ItemStack item, String path, String value) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key(path), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    public static String getString(ItemStack item, String path) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(key(path), PersistentDataType.STRING);
    }

    public static ItemStack setInt(ItemStack item, String path, int value) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key(path), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
        return item;
    }

    public static Integer getInt(ItemStack item, String path) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key(path), PersistentDataType.INTEGER);
    }

    public static ItemStack setBoolean(ItemStack item, String path, boolean value) {
        return setInt(item, path, value ? 1 : 0);
    }

    public static boolean getBoolean(ItemStack item, String path) {
        Integer i = getInt(item, path);
        return i != null && i == 1;
    }
}
