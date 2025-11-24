package com.crackshotv3.core.weapon;

/**
 * 武器の動的状態を管理
 */
public class WeaponState {

    private boolean isShooting;
    private boolean isReloading;
    private boolean isAimingDownSights;

    public WeaponState() {
        this.isShooting = false;
        this.isReloading = false;
        this.isAimingDownSights = false;
    }

    public boolean isShooting() { return isShooting; }
    public void setShooting(boolean shooting) { isShooting = shooting; }

    public boolean isReloading() { return isReloading; }
    public void setReloading(boolean reloading) { isReloading = reloading; }

    public boolean isAimingDownSights() { return isAimingDownSights; }
    public void setAimingDownSights(boolean aimingDownSights) { isAimingDownSights = aimingDownSights; }
}
