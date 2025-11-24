package com.crackshotv3.core.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 全武器の管理
 */
public class WeaponRegistry {

    private static final WeaponRegistry INSTANCE = new WeaponRegistry();

    private final Map<String, Weapon> weapons = new HashMap<>();

    private WeaponRegistry() {}

    public static WeaponRegistry get() { return INSTANCE; }

    public void registerWeapon(Weapon weapon) {
        weapons.put(weapon.getId(), weapon);
    }

    public Weapon getWeapon(String id) {
        return weapons.get(id);
    }

    public Collection<Weapon> getRegisteredWeapons() {
        return weapons.values();
    }

    public boolean isRegistered(String id) {
        return weapons.containsKey(id);
    }

    public void clear() {
        weapons.clear();
    }
}
