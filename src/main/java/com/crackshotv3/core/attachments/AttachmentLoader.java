package com.crackshotv3.core.attachments;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.configuration.file.FileConfiguration;

public final class AttachmentLoader {

    public static void loadAll(CrackShotV3 plugin) {
        FileConfiguration config = plugin.getConfig();

        if (!config.isConfigurationSection("attachments")) {
            LoggerUtil.warn("[AttachmentLoader] No attachments section found in config.");
            return;
        }

        AttachmentRegistry registry = AttachmentRegistry.get();

        config.getConfigurationSection("attachments").getKeys(false).forEach(id -> {
            String displayName = config.getString("attachments." + id + ".displayName", id);
            String typeName = config.getString("attachments." + id + ".type", "SCOPE");
            AttachmentType type = AttachmentType.valueOf(typeName.toUpperCase());

            AttachmentStats stats = AttachmentStats.DEFAULT; // デフォルト値使用

            Attachment attachment = new Attachment(id, displayName, type, stats);
            registry.register(attachment);

            LoggerUtil.info("[AttachmentLoader] Loaded attachment: " + id);
        });

        LoggerUtil.debug("[AttachmentLoader] All attachments loaded.");
    }
}
