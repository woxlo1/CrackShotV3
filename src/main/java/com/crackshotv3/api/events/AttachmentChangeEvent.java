package com.crackshotv3.api.events;

import com.crackshotv3.core.attachments.Attachment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AttachmentChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Attachment attachment;
    private final boolean equipped; // true = 装着, false = 解除

    public AttachmentChangeEvent(Player player, Attachment attachment, boolean equipped) {
        this.player = player;
        this.attachment = attachment;
        this.equipped = equipped;
    }

    public Player getPlayer() { return player; }
    public Attachment getAttachment() { return attachment; }
    public boolean isEquipped() { return equipped; }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
