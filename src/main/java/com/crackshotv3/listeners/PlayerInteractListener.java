package com.crackshotv3.listeners;

import com.crackshotv3.api.WeaponAPI;

/**
 * PlayerInteractListener
 *
 * 修正点:
 * 射撃・リロード処理は PlayerShootListener に統合した。
 * このクラスは登録しない（CrackShotV3.java から除外済み）。
 *
 * 将来的に PlayerInteract で武器以外の処理が必要になった場合のみ復活させる。
 * クラス自体は残しておくが Listener は実装しない。
 */
public class PlayerInteractListener {
    // 使用しない。CrackShotV3.java の registerListeners() から登録を外した。
}