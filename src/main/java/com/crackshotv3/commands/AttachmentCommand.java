package com.crackshotv3.commands;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentManager;
import com.crackshotv3.gui.AttachmentEditorGUI;
import com.crackshotv3.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AttachmentCommand implements CommandExecutor {

    private final CrackShotV3 plugin = CrackShotV3.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            MessageUtil.send(sender, "§c使用法: /attachment <edit|list> [アタッチメントID]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "edit" -> handleEdit(sender, args);
            case "list" -> handleList(sender);
            default -> MessageUtil.send(sender, "§c不明なサブコマンド: " + args[0]);
        }
        return true;
    }

    private void handleEdit(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, "§cプレイヤーのみがアタッチメントを編集できます。");
            return;
        }
        if (!sender.hasPermission("crackshotv3.admin")) {
            MessageUtil.send(player, "§c権限がありません。");
            return;
        }
        if (args.length < 2) {
            MessageUtil.send(player, "§c使用法: /attachment edit <アタッチメントID>");
            return;
        }
        Attachment attachment = AttachmentManager.get().getAttachment(args[1]);
        if (attachment == null) {
            MessageUtil.send(player, "§cアタッチメントが見つかりません: " + args[1]);
            return;
        }

        new AttachmentEditorGUI(plugin, player, attachment).open();
        MessageUtil.send(player, "§aアタッチメントエディターを開きました: " + attachment.getName());
    }

    private void handleList(CommandSender sender) {
        MessageUtil.send(sender, "§eロード済みアタッチメント一覧:");
        AttachmentManager.get().getAttachments().forEach(a ->
                MessageUtil.send(sender, " - " + a.getId() + " : " + a.getName())
        );
    }
}
