package com.crackshotv3.core.recoil;

import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * リコイルの中央管理エンジン（Singleton）
 *
 * - プレイヤーごとのリコイル状態を管理
 * - 毎tickで回復処理を行い、プレイヤー視点に差分を適用する
 */
public final class RecoilEngine {

    private static final RecoilEngine INSTANCE = new RecoilEngine();

    // プレイヤーUUID -> ActiveRecoil
    private final Map<java.util.UUID, ActiveRecoil> active = new ConcurrentHashMap<>();

    private BukkitTask tickTask;
    private Plugin plugin;
    private boolean initialized = false;

    private RecoilEngine() {}

    public static RecoilEngine get() { return INSTANCE; }

    /**
     * 初期化（プラグインの onEnable で呼ぶ）
     */
    public synchronized void init(Plugin plugin) {
        if (initialized) return;
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        tickTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
        initialized = true;
        LoggerUtil.info("[RecoilEngine] Initialized.");
    }

    /** シャットダウン（プラグインの onDisable で呼ぶ） */
    public synchronized void shutdown() {
        if (!initialized) return;
        if (tickTask != null) tickTask.cancel();
        active.clear();
        initialized = false;
        LoggerUtil.info("[RecoilEngine] Shutdown.");
    }

    /**
     * 単発リコイルを即時適用し回復スピードを設定する
     *
     * @param player         対象プレイヤー
     * @param pitchOffsetDeg 上方向に振る量（度）。下方向は負。
     * @param yawOffsetDeg   右方向に振る量（度）。左は負。
     * @param recoverySec    何秒でほぼ回復させたいか（秒）
     */
    public void applyImmediateRecoil(Player player, double pitchOffsetDeg, double yawOffsetDeg, double recoverySec) {
        if (player == null) return;
        ActiveRecoil ar = active.computeIfAbsent(player.getUniqueId(), k -> new ActiveRecoil(player));
        ar.addDelta(pitchOffsetDeg, yawOffsetDeg);
        ar.setRecoverySeconds(Math.max(0.05, recoverySec));
        LoggerUtil.debug("[RecoilEngine] Applied immediate recoil to " + player.getName()
                + " pitch=" + pitchOffsetDeg + " yaw=" + yawOffsetDeg + " recover=" + recoverySec + "s");
    }

    /**
     * パターンを適用（例えば連射：shotIndexは0始まり）
     *
     * @param player    対象
     * @param pattern   パターン
     * @param shotIndex 何発目か（patternの長さを超えると最後を返す）
     * @param recoverySec 回復所要秒数
     */
    public void applyPattern(Player player, RecoilPattern pattern, int shotIndex, double recoverySec) {
        if (player == null || pattern == null) return;
        RecoilPattern.RecoilOffset o = pattern.getOffset(shotIndex);
        applyImmediateRecoil(player, o.getPitch(), o.getYaw(), recoverySec);
    }

    /**
     * 毎tick処理：回復・プレイヤー視点適用
     */
    private void tick() {
        try {
            if (active.isEmpty()) return;

            long now = System.currentTimeMillis();
            for (Map.Entry<java.util.UUID, ActiveRecoil> e : active.entrySet()) {
                ActiveRecoil ar = e.getValue();
                Player player = ar.getPlayer();
                if (player == null || !player.isOnline()) {
                    active.remove(e.getKey());
                    continue;
                }

                // 回復処理
                ar.tick();

                // 差分の適用
                double pitchDelta = ar.getCurrentPitchDelta();
                double yawDelta = ar.getCurrentYawDelta();

                if (Math.abs(pitchDelta) < 1e-4 && Math.abs(yawDelta) < 1e-4) {
                    // ほぼゼロ: 状態が空なら消す
                    if (ar.isIdle()) {
                        active.remove(e.getKey());
                    }
                    continue;
                }

                // 現在のプレイヤーの向きに delta を足す（プレイヤー自身の入力とも競合するが、
                // この方式は多くのプラグインで使われている）
                Location loc = player.getLocation();
                float newPitch = clampPitch((float) (loc.getPitch() + pitchDelta));
                float newYaw = normalizeYaw((float) (loc.getYaw() + yawDelta));

                Location tp = loc.clone();
                tp.setPitch(newPitch);
                tp.setYaw(newYaw);

                // teleport は若干コストがあるが毎tickでも通常問題ない量（多数プレイヤー時は要注意）
                player.teleport(tp);
            }
        } catch (Throwable t) {
            LoggerUtil.error("[RecoilEngine] Error in tick()", t);
        }
    }

    /** プレイヤーから Recoil 状態を取り除く（強制解除） */
    public void clearRecoil(Player player) {
        if (player == null) return;
        active.remove(player.getUniqueId());
    }

    // ======= ヘルパ =======
    private static float clampPitch(float pitch) {
        if (pitch > 90f) return 90f;
        if (pitch < -90f) return -90f;
        return pitch;
    }

    private static float normalizeYaw(float yaw) {
        // keep yaw in [-180,180)
        while (yaw >= 180f) yaw -= 360f;
        while (yaw < -180f) yaw += 360f;
        return yaw;
    }

    // ======= ActiveRecoil 内部クラス =======
    private static final class ActiveRecoil {
        private final java.util.UUID uuid;
        private transient Player cachedPlayer;

        // 現在の差分（deg）
        private double currentPitchDelta = 0.0;
        private double currentYawDelta   = 0.0;

        // 回復設定：1秒あたりどれだけ差分を減らすか（deg/sec）
        private double recoveryDegPerSec = 20.0;

        // 内部の滑らかさ（0..1）: 大きいほどリニア回復、小さいほど指数的
        private double smoothing = 0.9;

        private long lastTick = System.currentTimeMillis();

        ActiveRecoil(Player p) {
            this.uuid = p.getUniqueId();
            this.cachedPlayer = p;
        }

        Player getPlayer() {
            if (cachedPlayer == null) cachedPlayer = Bukkit.getPlayer(uuid);
            return cachedPlayer;
        }

        synchronized void addDelta(double pitchDeg, double yawDeg) {
            currentPitchDelta += pitchDeg;
            currentYawDelta += yawDeg;
        }

        synchronized void setRecoverySeconds(double secs) {
            secs = Math.max(0.01, secs);
            // 合成リコイル量（絶対値合計）をsecsで回復する設定
            double total = Math.max(0.0001, Math.abs(currentPitchDelta) + Math.abs(currentYawDelta));
            this.recoveryDegPerSec = total / secs;
        }

        synchronized void tick() {
            long now = System.currentTimeMillis();
            double dt = Math.max(1, now - lastTick) / 1000.0;
            lastTick = now;

            // リニアに回復させる（だが滑らかさを入れて自然に）
            double recover = recoveryDegPerSec * dt;
            if (recover <= 0) return;

            // X 軸（pitch）
            if (Math.abs(currentPitchDelta) <= recover) {
                currentPitchDelta = 0;
            } else {
                currentPitchDelta -= Math.signum(currentPitchDelta) * recover;
            }

            // Y 軸（yaw）
            if (Math.abs(currentYawDelta) <= recover) {
                currentYawDelta = 0;
            } else {
                currentYawDelta -= Math.signum(currentYawDelta) * recover;
            }

            // optional: smoothing (small influence)
            currentPitchDelta *= smoothing + (1 - smoothing) * 0.0;
            currentYawDelta   *= smoothing + (1 - smoothing) * 0.0;
        }

        synchronized double getCurrentPitchDelta() { return currentPitchDelta; }
        synchronized double getCurrentYawDelta()   { return currentYawDelta; }

        synchronized boolean isIdle() {
            return Math.abs(currentPitchDelta) < 1e-4 && Math.abs(currentYawDelta) < 1e-4;
        }
    }
}
