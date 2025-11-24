package com.crackshotv3.core.attachments;

import com.crackshotv3.core.util.LoggerUtil;
import com.crackshotv3.core.weapon.Weapon;

import java.util.Objects;

/**
 * アタッチメント定義
 */
public final class Attachment {

    private final String id;
    private final String displayName;
    private final AttachmentType type;
    private final AttachmentStats stats;

    public Attachment(String id, String displayName, AttachmentType type, AttachmentStats stats) {
        this.id = Objects.requireNonNull(id, "id");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.type = Objects.requireNonNull(type, "type");
        this.stats = stats != null ? stats : AttachmentStats.DEFAULT;

        LoggerUtil.debug("[Attachment] Created attachment: " + id);
    }

    public void applyToWeapon(Weapon weapon) {
        if (weapon == null) return;
        weapon.getStats().applyModifier(stats);
    }

    public void removeFromWeapon(Weapon weapon) {
        if (weapon == null) return;
        weapon.getStats().resetToBase();
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public AttachmentType getType() { return type; }
    public AttachmentStats getStats() { return stats; }

    public String getName() {
        return getDisplayName();
    }
}
