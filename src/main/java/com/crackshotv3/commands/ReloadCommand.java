package com.crackshotv3.commands;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.WeaponLoader;
import com.crackshotv3.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final CrackShotV3 plugin = CrackShotV3.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("crackshotv3.admin")) {
            MessageUtil.send(sender, "§c権限がありません。");
            return true;
        }

        MessageUtil.send(sender, "§e全武器をリロード中...");
        WeaponLoader.loadAll(plugin);
        MessageUtil.send(sender, "§a武器のリロードが完了しました！");
        return true;
    }
}
