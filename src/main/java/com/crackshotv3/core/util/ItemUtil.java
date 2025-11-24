package com.crackshotv3.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {

    public static ItemStack createItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createWeapon(Material mat, String weaponId) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§e" + weaponId);
        item.setItemMeta(meta);

        NBTUtil.setString(item, "weapon-id", weaponId);
        return item;
    }

    public static boolean isWeapon(ItemStack item) {
        return NBTUtil.getString(item, "weapon-id") != null;
    }

    public static String getWeaponId(ItemStack item) {
        return NBTUtil.getString(item, "weapon-id");
    }
}
