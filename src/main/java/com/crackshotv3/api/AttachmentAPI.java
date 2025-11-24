package com.crackshotv3.api;

import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentManager;
import org.bukkit.entity.Player;

public class AttachmentAPI {

    private final AttachmentManager manager;

    public AttachmentAPI(AttachmentManager manager) {
        this.manager = manager;
    }

    /**
     * アタッチメントを装着
     */
    public void equipAttachment(Player player, Attachment attachment) {
        if (attachment == null) return;
        manager.equip(player, attachment);
    }

    /**
     * アタッチメントを解除
     */
    public void removeAttachment(Player player, Attachment attachment) {
        if (attachment == null) return;
        manager.remove(player, attachment);
    }
}
