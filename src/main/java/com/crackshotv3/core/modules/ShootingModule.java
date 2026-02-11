package com.crackshotv3.core.modules;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.core.weapon.Weapon;
import com.crackshotv3.core.util.LoggerUtil;
import org.bukkit.entity.Player;

import static com.crackshotv3.core.util.MessageUtil.send;

/**
 * ShootingModule
 *
 * 修正点:
 * 以前は onShoot() 内で ProjectileEngine に直接弾丸を登録していたが、
 * 実際の弾丸生成は PlayerShootListener → ProjectileAPI.spawnProjectile() で行われる。
 * weapon.shoot() は Weapon クラスから呼ばれ、ModuleManager.triggerShoot() → このクラスの onShoot() が呼ばれる。
 * ここで再度弾丸を生成すると二重発射になるため、弾丸生成コードを削除。
 *
 * このモジュールは射撃時の副次処理（ジャム確認・オーバーヒート確認・ログ出力）のみ担当する。
 */
public class ShootingModule extends Module {

    public ShootingModule(CrackShotV3 plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "ShootingModule";
    }

    @Override
    public void onShoot(Player player, Weapon weapon) {
        if (player == null || weapon == null) return;

        // ジャム・オーバーヒートチェック（既に Weapon.canShoot() でも確認しているが念のため）
        if (weapon.isJammed()) {
            send(player, "§c武器がジャムしています！");
            debug(player.getName() + " tried to fire jammed weapon: " + weapon.getId());
            return;
        }

        if (weapon.isOverheated()) {
            send(player, "§c武器がオーバーヒートしています！");
            debug(player.getName() + " tried to fire overheated weapon: " + weapon.getId());
            return;
        }

        // 弾丸生成は PlayerShootListener → ProjectileAPI に任せる
        // ここでは発射ログのみ記録
        debug(player.getName() + " fired " + weapon.getId());
    }
}