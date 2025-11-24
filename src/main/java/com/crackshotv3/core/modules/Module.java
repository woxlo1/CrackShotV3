package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.entity.Player;

/**
 * 全モジュールの基底クラス。
 * 特定のイベントハンドラのみ必要に応じて override する。
 */
public abstract class Module {

    protected final CrackShotV3 plugin;

    public Module(CrackShotV3 plugin) {
        this.plugin = plugin;
    }

    /** モジュール名（ログや識別用） */
    public abstract String getName();

    /** 武器が読み込まれた時 */
    public void onWeaponLoad(Weapon weapon) {}

    /** プレイヤーが武器を撃った時 */
    public void onShoot(Player player, Weapon weapon) {}

    /** プレイヤーがリロードした時 */
    public void onReload(Player player, Weapon weapon) {}

    public void onReloadStart(Player player, Weapon weapon) {}

    public void onReloadFinish(Player player, Weapon weapon) {}

    /** 毎Tick処理（必要なモジュールだけ使用する） */
    public void onTick(Player player, Weapon weapon) {}

    /** モジュール共通のデバッグログ */
    protected void debug(String msg) {
        LoggerUtil.debug("[" + getName() + "] " + msg);
    }
}
