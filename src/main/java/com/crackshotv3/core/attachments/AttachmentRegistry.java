package com.crackshotv3.core.attachments;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全アタッチメントの登録
 */
public final class AttachmentRegistry {

    private static final AttachmentRegistry INSTANCE = new AttachmentRegistry();
    private final Map<String, Attachment> attachments = new HashMap<>();

    private AttachmentRegistry() {}

    public static AttachmentRegistry get() { return INSTANCE; }

    /**
     * アタッチメントを登録
     */
    public void registerAttachment(Attachment attachment) {
        if (attachment == null) return;
        attachments.put(attachment.getId(), attachment);
    }

    /**
     * 便利メソッドとして register() を追加（registerAttachment と同じ）
     */
    public void register(Attachment attachment) {
        registerAttachment(attachment);
    }

    public Attachment getAttachment(String id) {
        return attachments.get(id);
    }

    public Map<String, Attachment> getAllAttachments() {
        return Collections.unmodifiableMap(attachments);
    }
}
