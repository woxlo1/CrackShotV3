package com.crackshotv3.commands;

import com.crackshotv3.core.projectile.ProjectileEngine;
import com.crackshotv3.core.weapon.WeaponRegistry;
import com.crackshotv3.core.attachments.AttachmentManager;
import com.crackshotv3.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MessageUtil.send(sender, "§e===== CSV3 デバッグ情報 =====");

        MessageUtil.send(sender, "§6武器:");
        WeaponRegistry.get().getRegisteredWeapons().forEach(w ->
                MessageUtil.send(sender, " - " + w.getId() + " : " + w.getDisplayName())
        );

        MessageUtil.send(sender, "§6アタッチメント:");
        AttachmentManager.get().getAttachments().forEach(a ->
                MessageUtil.send(sender, " - " + a.getId() + " : " + a.getName())
        );

        MessageUtil.send(sender, "§6弾丸:");
        ProjectileEngine.getProjectiles().forEach(p ->
                MessageUtil.send(sender, " - " + p.getId() + " : 速度=" + p.getSpeed())
        );

        MessageUtil.send(sender, "§e==========================");

        return true;
    }
}
