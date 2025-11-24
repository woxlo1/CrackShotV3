package com.crackshotv3.core.animation;

import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全アニメーション管理エンジン
 */
public final class AnimationEngine {

    private static final AnimationEngine INSTANCE = new AnimationEngine();
    private final Map<Player, PlayerAnimation> activeAnimations = new ConcurrentHashMap<>();
    private BukkitTask tickTask;
    private Plugin plugin;
    private boolean initialized = false;

    private AnimationEngine() {}

    public static AnimationEngine get() { return INSTANCE; }

    public synchronized void init(Plugin plugin) {
        if (initialized) return;
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        tickTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
        initialized = true;
        LoggerUtil.info("[AnimationEngine] Initialized");
    }

    public synchronized void shutdown() {
        if (!initialized) return;
        if (tickTask != null) tickTask.cancel();
        activeAnimations.clear();
        initialized = false;
        LoggerUtil.info("[AnimationEngine] Shutdown");
    }

    public void playAnimation(Player player, AnimationSequence sequence, boolean firstPerson) {
        if (player == null || sequence == null) return;
        PlayerAnimation pa = activeAnimations.computeIfAbsent(player, k -> new PlayerAnimation(player));
        pa.play(sequence, firstPerson);
    }

    public void playFirstPerson(Player player, FirstPersonAnimation animation) {
        if (player == null || animation == null) return;
        AnimationSequence seq = animation.toSequence();
        playAnimation(player, seq, true);
    }

    private void tick() {
        try {
            for (Map.Entry<Player, PlayerAnimation> e : activeAnimations.entrySet()) {
                PlayerAnimation pa = e.getValue();
                Player player = pa.getPlayer();
                if (player == null || !player.isOnline()) {
                    activeAnimations.remove(e.getKey());
                    continue;
                }
                pa.tick();
            }
        } catch (Throwable t) {
            LoggerUtil.error("[AnimationEngine] Tick error", t);
        }
    }

    // ==========================
    //   内部クラス: プレイヤーごとのアニメ管理
    // ==========================
    private static final class PlayerAnimation {

        private final Player player;
        private AnimationSequence sequence;
        private boolean firstPerson;
        private long tickCounter = 0;
        private boolean playing = false;

        PlayerAnimation(Player player) {
            this.player = player;
        }

        void play(AnimationSequence sequence, boolean firstPerson) {
            this.sequence = sequence;
            this.firstPerson = firstPerson;
            this.tickCounter = 0;
            this.playing = true;
            LoggerUtil.debug("[AnimationEngine] Playing animation for player " + player.getName()
                    + ", firstPerson=" + firstPerson);
        }

        void tick() {
            if (!playing || sequence == null) return;

            // 指定 tick の Transform を取得
            AnimationSequence.Transform t = sequence.getTransformAt(tickCounter);
            if (t == null) {
                // アニメ終了
                playing = false;
                LoggerUtil.debug("[AnimationEngine] Animation ended for player " + player.getName());
                return;
            }

            // ==========================
            // 実際の武器モデルに Transform を適用
            // ==========================
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() != null) {
                // カスタムモデルデータは既存 ModelStateHandler で管理されている想定
                String modelId = ModelStateHandler.get().getModel(player);
                if (modelId != null) {
                    // ここでは擬似的にモデルデータと Transform を適用
                    // 実際は Packet / ProtocolLib / NMS 等で FirstPerson/ThirdPerson 表示を操作する
                    applyTransformToItem(item, t, firstPerson);
                }
            }

            tickCounter++;
        }

        private void applyTransformToItem(ItemStack item, AnimationSequence.Transform transform, boolean firstPerson) {
            // 仮実装: ログに出力
            LoggerUtil.debug(String.format("[AnimationEngine] Applying transform to player=%s, item=%s, FP=%s -> pos=%s rot=%s scale=%s",
                    player.getName(),
                    item.getType(),
                    firstPerson,
                    transform.getPosition(),
                    transform.getRotation(),
                    transform.getScale()));
            // 実際には ProtocolLib でパケットを送信するか、カスタムリソースパックモデルに適用
        }

        Player getPlayer() { return player; }
    }
}
