package com.crackshotv3.api;

import com.crackshotv3.core.util.NBTUtil;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.core.weapon.WeaponState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WeaponAPI
 *
 * 修正点:
 * getWeaponFromItem() が displayName で武器を識別していたため、
 * 同名の武器が複数存在した場合に誤判定していた。
 * NBTUtil.getWeaponId() を使って PersistentDataContainer から weapon-id を取得する方式に変更。
 *
 * 合わせて WeaponCommand.handleGive() で NBT を埋め込む処理が必要（WeaponCommand.java を修正済み）。
 */
public class WeaponAPI {

    private final WeaponRegistry registry;

    private final Map<Player, Weapon> equippedWeapons = new HashMap<>();

    public WeaponAPI(WeaponRegistry registry) {
        this.registry = registry;
    }

    // =====================================================
    // 武器操作
    // =====================================================

    public void fireWeapon(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        weapon.shoot(player);
    }

    public void reloadWeapon(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        weapon.startReload(player);
    }

    public void toggleADS(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;
        WeaponState state = weapon.getCurrentState();
        state.setAimingDownSights(!state.isAimingDownSights());
    }

    // =====================================================
    // 武器取得
    // =====================================================

    public Weapon getWeapon(String id) {
        if (id == null) return null;
        return registry.getWeapon(id);
    }

    /**
     * 修正: DisplayName ではなく NBT (weapon-id) で識別する。
     * ItemUtil.createWeapon() で NBTUtil.setString(item, "weapon-id", weaponId) が呼ばれていることが前提。
     * WeaponCommand.handleGive() でも NBT を埋め込むよう修正済み。
     */
    public Weapon getWeaponFromItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;

        // NBT から weapon-id を取得
        String weaponId = NBTUtil.getString(itemStack, "weapon-id");
        if (weaponId == null || weaponId.isEmpty()) return null;

        return registry.getWeapon(weaponId);
    }

    public Weapon getEquippedWeapon(Player player) {
        if (player == null) return null;

        Weapon equipped = equippedWeapons.get(player);

        if (equipped == null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            equipped = getWeaponFromItem(item);
            if (equipped != null) {
                equippedWeapons.put(player, equipped);
            }
        }

        return equipped;
    }

    public void unequipWeapon(Player player) {
        if (player == null) return;
        equippedWeapons.remove(player);
    }

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

    public Map<Player, Weapon> getEquippedWeapons() {
        return Collections.unmodifiableMap(equippedWeapons);
    }
}