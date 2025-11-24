package com.crackshotv3.core.attachments;

import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AttachmentManager {

    private static final AttachmentManager INSTANCE = new AttachmentManager();

    // Player -> slot type -> Attachment
    private final Map<Player, Map<AttachmentType, Attachment>> equipped = new ConcurrentHashMap<>();

    // 全アタッチメントをIDで管理
    private final Map<String, Attachment> attachmentsById = new ConcurrentHashMap<>();

    private AttachmentManager() {}

    public static AttachmentManager get() { return INSTANCE; }

    // -------------------
    // プレイヤー装着系
    // -------------------
    public void equip(Player player, Attachment attachment) {
        if (player == null || attachment == null) return;
        equipped.computeIfAbsent(player, k -> new ConcurrentHashMap<>())
                .put(attachment.getType(), attachment);
    }

    public void unequip(Player player, AttachmentType type) {
        if (player == null || type == null) return;
        Map<AttachmentType, Attachment> playerAttachments = equipped.get(player);
        if (playerAttachments != null) playerAttachments.remove(type);
    }

    public void remove(Player player, Attachment attachment) {
        if (player == null || attachment == null) return;
        unequip(player, attachment.getType());
        LoggerUtil.debug("[AttachmentManager] Player " + player.getName() + " removed attachment " + attachment.getId());
    }

    public Attachment getEquipped(Player player, AttachmentType type) {
        if (player == null || type == null) return null;
        Map<AttachmentType, Attachment> playerAttachments = equipped.get(player);
        if (playerAttachments == null) return null;
        return playerAttachments.get(type);
    }

    // -------------------
    // 全アタッチメント管理系
    // -------------------
    public void registerAttachment(Attachment attachment) {
        if (attachment == null) return;
        attachmentsById.put(attachment.getId(), attachment);
    }

    public Attachment getAttachment(String id) {
        if (id == null) return null;
        return attachmentsById.get(id);
    }

    public Collection<Attachment> getAttachments() {
        return attachmentsById.values();
    }
}
