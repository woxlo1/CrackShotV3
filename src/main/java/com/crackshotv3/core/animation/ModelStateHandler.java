package com.crackshotv3.core.animation;

import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 武器モデル切替管理
 * プレイヤーの手持ち武器のカスタムモデルデータを切り替える
 */
public final class ModelStateHandler {

    private static final ModelStateHandler INSTANCE = new ModelStateHandler();

    private final Map<Player, String> playerModelState = new ConcurrentHashMap<>();

    private ModelStateHandler() {}

    public static ModelStateHandler get() { return INSTANCE; }

    /**
     * 武器モデルを切替える
     *
     * @param player  対象プレイヤー
     * @param modelId モデルID（整数またはプレ定義IDを文字列で扱う）
     */
    public void setModel(Player player, String modelId) {
        if (player == null || modelId == null) return;

        // 現在の手持ちアイテムを取得
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            LoggerUtil.warn("[ModelStateHandler] Player " + player.getName() + " is not holding a valid item!");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // モデルIDを整数に変換（例: "AK47" → 101）
        int modelData;
        try {
            modelData = Integer.parseInt(modelId);
        } catch (NumberFormatException e) {
            LoggerUtil.warn("[ModelStateHandler] Invalid modelId for player " + player.getName() + ": " + modelId);
            return;
        }

        meta.setCustomModelData(modelData);
        item.setItemMeta(meta);

        playerModelState.put(player, modelId);

        LoggerUtil.debug("[ModelStateHandler] Player " + player.getName() + " model set to " + modelId);
    }

    /**
     * 現在のモデルIDを取得
     *
     * @param player 対象プレイヤー
     * @return モデルID、未設定なら null
     */
    public String getModel(Player player) {
        if (player == null) return null;
        return playerModelState.get(player);
    }

    /**
     * モデルをリセット（標準アイテム状態に戻す）
     *
     * @param player 対象プレイヤー
     */
    public void clear(Player player) {
        if (player == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(null);
            item.setItemMeta(meta);
        }

        playerModelState.remove(player);

        LoggerUtil.debug("[ModelStateHandler] Cleared model for player " + player.getName());
    }
}
