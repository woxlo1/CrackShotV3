package com.crackshotv3.api;

import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.core.weapon.WeaponState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WeaponAPI
 * プレイヤーの武器装備・射撃・リロード・ADS操作を管理するAPI。
 */
public class WeaponAPI {

    private final WeaponRegistry registry;

    // プレイヤーごとの装備中武器
    private final Map<Player, Weapon> equippedWeapons = new HashMap<>();

    public WeaponAPI(WeaponRegistry registry) {
        this.registry = registry;
    }

    // =====================================================
    // 武器操作
    // =====================================================

    /** プレイヤーが武器を発射 */
    public void fireWeapon(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        weapon.shoot(player);
    }

    /** 武器のリロード */
    public void reloadWeapon(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        weapon.startReload(player);
    }

    /** 武器のADS切替（Aim Down Sights） */
    public void toggleADS(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        WeaponState state = weapon.getCurrentState();
        state.setAimingDownSights(!state.isAimingDownSights());
    }

    // =====================================================
    // 武器取得
    // =====================================================

    /** IDから武器を取得 */
    public Weapon getWeapon(String id) {
        if (id == null) return null;
        return registry.getWeapon(id);
    }

    /** ItemStackからWeaponを取得 */
    public Weapon getWeaponFromItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;

        String displayName = meta.getDisplayName();

        for (Weapon weapon : registry.getRegisteredWeapons()) {
            if (weapon.getDisplayName().equals(displayName)) {
                return weapon;
            }
        }
        return null;
    }


    /** プレイヤーの装備中武器を取得 */
    public Weapon getEquippedWeapon(Player player) {
        if (player == null) return null;

        Weapon equipped = equippedWeapons.get(player);

        // 装備が未登録ならメインハンドのアイテムから自動判定
        if (equipped == null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            equipped = getWeaponFromItem(item);
            if (equipped != null) {
                equippedWeapons.put(player, equipped);
            }
        }

        return equipped;
    }

    /** プレイヤーの装備中武器を解除 */
    public void unequipWeapon(Player player) {
        if (player == null) return;
        equippedWeapons.remove(player);
    }

    /** プレイヤーに武器を装備させる */
    public void equipWeapon(Player player, Weapon weapon) {
        if (player == null) return;
        if (weapon == null) {
            equippedWeapons.remove(player);
        } else {
            equippedWeapons.put(player, weapon);
        }
    }

    // =====================================================
    // Utility
    // =====================================================

    /** 全プレイヤーの装備情報を取得（読み取り専用） */
    public Map<Player, Weapon> getEquippedWeapons() {
        return Collections.unmodifiableMap(equippedWeapons);
    }
}
