package com.crackshotv3.gui;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.attachments.Attachment;
import com.crackshotv3.core.attachments.AttachmentRegistry;
import com.crackshotv3.core.util.MessageUtil;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.gui.base.BaseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AttachmentEditorGUI extends BaseGUI {

    private final Weapon weapon; // null allowed -> list global
    private final Attachment attachment; // null allowed
    private final AttachmentRegistry registry = AttachmentRegistry.get();

    public AttachmentEditorGUI(CrackShotV3 plugin, Player player, Attachment attachment) {
        super(plugin, player);
        this.weapon = null;
        this.attachment = attachment;
    }

    @Override
    public String getTitle() { return "§6Attachment Editor"; }

    @Override
    public int getSize() { return 54; }

    @Override
    public void draw() {
        fillBackground();
        int slot = 10;
        for (Attachment a : registry.getAllAttachments().values()) {
            boolean equipped = (weapon != null && weapon.getAttachments().contains(a));
            setItem(slot, item(Material.PAPER, (equipped ? "§a[E] " : "§f") + a.getDisplayName(),
                    "§7id: " + a.getId(), "§7type: " + a.getType().name()), ev -> {
                if (weapon != null) {
                    if (equipped) weapon.removeAttachment(a);
                    else weapon.addAttachment(a);
                    redraw();
                } else {
                    // open detail editing (very simple)
                    MessageUtil.send(player,"Attachment: " + a.getId() + " -> edit not implemented here");
                }
            });
            slot++;
            if ((slot % 9) == 0) slot += 2;
            if (slot >= 44) break;
        }
        addBackButton(53, () -> player.closeInventory());
    }
}
