package com.crackshotv3.gui;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.gui.base.BaseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;

public class ProjectileEditorGUI extends BaseGUI {

    private final Supplier<Double> getSpeed;
    private final DoubleConsumer setSpeed;
    private final Supplier<Double> getGravity;
    private final DoubleConsumer setGravity;
    private final Supplier<Double> getDamage;
    private final DoubleConsumer setDamage;

    public ProjectileEditorGUI(CrackShotV3 plugin, Player player,
                               Supplier<Double> getSpeed, DoubleConsumer setSpeed,
                               Supplier<Double> getGravity, DoubleConsumer setGravity,
                               Supplier<Double> getDamage, DoubleConsumer setDamage) {
        super(plugin, player);
        this.getSpeed = getSpeed;
        this.setSpeed = setSpeed;
        this.getGravity = getGravity;
        this.setGravity = setGravity;
        this.getDamage = getDamage;
        this.setDamage = setDamage;
    }

    @Override
    public String getTitle() { return "§6Projectile Editor"; }

    @Override
    public int getSize() { return 27; }

    @Override
    public void draw() {
        fillBackground();
        setItem(10, item(Material.ARROW, "§aSpeed: §f" + getSpeed.get(), "§7クリックで変更"),
                ev -> new NumericInputGUI(plugin, player, "Speed", getSpeed.get(), 0.01, 200.0, setSpeed).open());
        setItem(13, item(Material.FEATHER, "§aGravity Y: §f" + getGravity.get(), "§7クリックで変更"),
                ev -> new NumericInputGUI(plugin, player, "Gravity", getGravity.get(), -10.0, 10.0, setGravity).open());
        setItem(16, item(Material.BLAZE_POWDER, "§aDamage: §f" + getDamage.get(), "§7クリックで変更"),
                ev -> new NumericInputGUI(plugin, player, "Damage", getDamage.get(), 0.0, 1000.0, setDamage).open());
        addBackButton(26, () -> player.closeInventory());
    }
}
