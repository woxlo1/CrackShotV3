package com.crackshotv3.commands;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.util.NBTUtil;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.gui.WeaponEditorGUI;
import com.crackshotv3.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * WeaponCommand
 *
 * 修正点:
 * handleGive() で ItemStack を生成する際に NBTUtil.setString(item, "weapon-id", weapon.getId()) を
 * 追加し、WeaponAPI.getWeaponFromItem() が NBT で識別できるようにした。
 */
public class WeaponCommand implements CommandExecutor {

    private final CrackShotV3 plugin = CrackShotV3.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            MessageUtil.send(sender, "§e使用法: /weapon <edit|give|list> [武器ID]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "edit" -> handleEdit(sender, args);
            case "give" -> handleGive(sender, args);
            case "list" -> handleList(sender);
            default -> MessageUtil.send(sender, "§c不明なサブコマンド: " + args[0]);
        }
        return true;
    }

    private void handleEdit(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "§cプレイヤーのみが武器を編集できます。");
            return;
        }
        if (!sender.hasPermission("crackshotv3.admin")) {
            MessageUtil.send(player, "§c権限がありません。");
            return;
        }
        if (args.length < 2) {
            MessageUtil.send(player, "§c使用法: /weapon edit <武器ID>");
            return;
        }
        Weapon weapon = WeaponRegistry.get().getWeapon(args[1]);
        if (weapon == null) {
            MessageUtil.send(player, "§c武器が見つかりません: " + args[1]);
            return;
        }

        new WeaponEditorGUI(plugin, player, weapon).open();
        MessageUtil.send(player, "§a武器エディターを開きました: " + weapon.getDisplayName());
    }

    private void handleGive(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "§cプレイヤーのみが武器を受け取れます。");
            return;
        }
        if (!sender.hasPermission("crackshotv3.user")) {
            MessageUtil.send(player, "§c権限がありません。");
            return;
        }
        if (args.length < 2) {
            MessageUtil.send(player, "§c使用法: /weapon give <武器ID>");
            return;
        }
        Weapon weapon = WeaponRegistry.get().getWeapon(args[1]);
        if (weapon == null) {
            MessageUtil.send(player, "§c武器が見つかりません: " + args[1]);
            return;
        }

        ItemStack item = new ItemStack(weapon.getItemMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(weapon.getDisplayName());
            item.setItemMeta(meta);
        }

        // 修正: NBT に weapon-id を埋め込む（WeaponAPI.getWeaponFromItem() がNBTで識別するため）
        NBTUtil.setString(item, "weapon-id", weapon.getId());

        player.getInventory().addItem(item);
        MessageUtil.send(player, "§a武器を受け取りました: " + weapon.getDisplayName());
    }

    private void handleList(CommandSender sender) {
        MessageUtil.send(sender, "§eロード済みの武器一覧:");
        WeaponRegistry.get().getRegisteredWeapons().forEach(w ->
                MessageUtil.send(sender, " - " + w.getId() + " : " + w.getDisplayName())
        );
    }
}