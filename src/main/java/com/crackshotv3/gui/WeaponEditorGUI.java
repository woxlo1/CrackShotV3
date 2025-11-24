package com.crackshotv3.gui;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentRegistry;
import com.crackshotv3.core.util.MessageUtil;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponStats;
import com.crackshotv3.core.weapon.WeaponSaver;
import com.crackshotv3.core.storage.CommentedWeaponYamlGenerator;
import com.crackshotv3.core.util.LoggerUtil;
import com.crackshotv3.gui.base.BaseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;

public class WeaponEditorGUI extends BaseGUI {

    private final Weapon weapon; // Weapon専用
    private final Attachment attachment; // 今回はWeaponEditorでは通常不要
    private final AttachmentRegistry registry = AttachmentRegistry.get();

    // Weapon 専用コンストラクタ
    public WeaponEditorGUI(CrackShotV3 plugin, Player player, Weapon weapon) {
        super(plugin, player);
        this.weapon = weapon;
        this.attachment = null;
    }

    @Override
    public String getTitle() {
        return "§6Weapon Editor: " + weapon.getId();
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public void draw() {
        fillBackground();

        WeaponStats s = weapon.getStats();

        // 武器基本情報
        setItem(13, item(Material.PAPER,
                "§e" + weapon.getDisplayName(),
                "§7id: " + weapon.getId(),
                "§7mat: " + weapon.getItemMaterial().name()));

        // ダメージ
        setItem(11, item(Material.IRON_SWORD, "§aDamage: §f" + s.getDamage(), "§7クリックで変更"),
                ev -> openNumeric("Damage", () -> s.getDamage(), v -> {
                    s.setDamage(v);
                    redraw();
                }, 0, 10000));

        // 発射速度
        setItem(12, item(Material.CLOCK, "§aFire Rate: §f" + s.getFireRate(), "§7クリックで変更"),
                ev -> openNumeric("Fire Rate", () -> s.getFireRate(), v -> {
                    s.setFireRate(v);
                    redraw();
                }, 0.001, 10));

        // 反動
        setItem(14, item(Material.SPYGLASS, "§aRecoil V/H: §f" + s.getRecoilVertical() + " / " + s.getRecoilHorizontal(), "§7クリックで RecoilEditor"),
                ev -> new RecoilEditorGUI(plugin, player,
                        () -> s.getRecoilVertical(),
                        v -> { s.setRecoilVertical(v); redraw(); },
                        () -> s.getRecoilHorizontal(),
                        v -> { s.setRecoilHorizontal(v); redraw(); })
                        .open());

        // 弾種
        setItem(20, item(Material.ARROW, "§aProjectile: §f" + s.getProjectileType(), "§7クリックで切替"),
                ev -> { cycleProjectileType(); redraw(); });

        // スコープ
        setItem(21, item(Material.SPYGLASS, "§aScope Zoom FOV: §f" + weapon.getScopeZoomFOV(), "§7クリックで変更"),
                ev -> openNumeric("Scope FOV", () -> (double) weapon.getScopeZoomFOV(),
                        v -> { weapon.setScope(true); weapon.setScopeZoomFOV((float)v); redraw(); }, 5, 120));

        // マガジン容量
        setItem(22, item(Material.LECTERN, "§aMagazine: §f" + weapon.getMaxAmmo(), "§7クリックで変更"),
                ev -> openNumeric("Magazine", () -> (double) weapon.getMaxAmmo(),
                        v -> { weapon.setMaxAmmo((int)Math.max(1, Math.round(v))); redraw(); }, 1, 2048));

        // アタッチメント編集
        setItem(29, item(Material.BEACON, "§aAttachments: §f" + weapon.getAttachments().size(), "§7クリックで編集"),
                ev -> new AttachmentEditorGUI(plugin, player, attachment).open());

        // 保存ボタン
        setItem(49, item(Material.EMERALD, "§aSave & Close", "§7クリックで保存して閉じる"),
                ev -> {
                    if (!validate()) {
                        MessageUtil.send(player,"§cValidation failed. Check values.");
                        return;
                    }
                    WeaponSaver.saveWeapon(plugin, weapon);
                    CommentedWeaponYamlGenerator.generate(plugin.getDataFolder(), weapon);
                    MessageUtil.send(player,"§aSaved: " + weapon.getId());
                    LoggerUtil.info("[WeaponEditorGUI] Saved " + weapon.getId() + " by " + player.getName());
                    player.closeInventory();
                });

        // 閉じるボタン
        setItem(50, item(Material.BARRIER, "§cClose (no save)"), ev -> player.closeInventory());
        addBackButton(53, () -> player.closeInventory());
    }

    /** 弾種を順番に切替 */
    private void cycleProjectileType() {
        String cur = weapon.getStats().getProjectileType();
        String[] types = new String[] {"hitscan", "physical", "explosive", "piercing", "beam"};
        int idx = 0;
        for (int i=0;i<types.length;i++) if (types[i].equalsIgnoreCase(cur)) { idx = i; break; }
        weapon.getStats().setProjectileType(types[(idx+1)%types.length]);
    }

    /** 値のバリデーション */
    private boolean validate() {
        WeaponStats s = weapon.getStats();
        if (s.getDamage() < 0) return false;
        if (s.getFireRate() <= 0) return false;
        if (weapon.getMaxAmmo() <= 0) return false;
        return true;
    }

    /** 数値入力 GUI を開くユーティリティ */
    private void openNumeric(String title, Supplier<Double> getter, DoubleConsumer setter, double min, double max) {
        new NumericInputGUI(plugin, player, title, getter.get(), min, max, setter).open();
    }
}
