package com.crackshotv3.core.util;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.attachments.AttachmentLoader;
import com.crackshotv3.core.weapon.WeaponLoader;
import com.crackshotv3.core.util.LoggerUtil;

import java.io.File;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class HotReloadService implements Runnable {

    private final CrackShotV3 plugin;
    private final Path watchPath;
    private volatile boolean running = true;
    private Thread thread;

    public HotReloadService(CrackShotV3 plugin) {
        this.plugin = plugin;
        this.watchPath = new File(plugin.getDataFolder(), "weapons").toPath();
        if (!Files.exists(watchPath)) {
            watchPath.toFile().mkdirs();
        }
    }

    public void start() {
        thread = new Thread(this, "CSV3-HotReload");
        thread.setDaemon(true);
        thread.start();
        LoggerUtil.info("[HotReload] Started watching: " + watchPath.toString());
    }

    public void stop() {
        running = false;
        if (thread != null) thread.interrupt();
        LoggerUtil.info("[HotReload] Stopped.");
    }

    @Override
    public void run() {
        try (WatchService ws = FileSystems.getDefault().newWatchService()) {
            watchPath.register(ws, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            while (running) {
                WatchKey key = ws.take();
                for (WatchEvent<?> ev : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = ev.kind();
                    Path changed = ((WatchEvent<Path>) ev).context();
                    String name = changed.toString();
                    LoggerUtil.debug("[HotReload] Detected " + kind.name() + ": " + name);

                    // 小さな遅延を入れてファイル書き込み完了を待つ
                    Thread.sleep(120);

                    // 実際の処理: 再ロード
                    try {
                        WeaponLoader.loadAll(plugin);
                        AttachmentLoader.loadAll(plugin);
                        // Optionally call ModuleManager reload if you expose it
                        LoggerUtil.info("[HotReload] Reloaded weapons & attachments due to " + kind.name() + ": " + name);
                    } catch (Exception e) {
                        LoggerUtil.error("[HotReload] Error reloading after change: " + name, e);
                    }
                }
                key.reset();
            }
        } catch (InterruptedException ie) {
            // shutdown
        } catch (Exception e) {
            LoggerUtil.error("[HotReload] WatchService error", e);
        }
    }
}
