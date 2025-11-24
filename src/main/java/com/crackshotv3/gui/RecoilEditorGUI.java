package com.crackshotv3.gui;

import com.crackshotv3.CrackShotV3;
import com.crackshotv3.gui.base.BaseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;

public class RecoilEditorGUI extends BaseGUI {

    private final Supplier<Double> getVert;
    private final DoubleConsumer setVert;
    private final Supplier<Double> getHoriz;
    private final DoubleConsumer setHoriz;

    public RecoilEditorGUI(CrackShotV3 plugin, Player player,
                           Supplier<Double> getVert, DoubleConsumer setVert,
                           Supplier<Double> getHoriz, DoubleConsumer setHoriz) {
        super(plugin, player);
        this.getVert = getVert;
        this.setVert = setVert;
        this.getHoriz = getHoriz;
        this.setHoriz = setHoriz;
    }

    @Override
    public String getTitle() { return "§6Recoil Editor"; }

    @Override
    public int getSize() { return 27; }

    @Override
    public void draw() {
        fillBackground();
        setItem(11, item(Material.OAK_BUTTON, "§aVertical: §f" + getVert.get(),"§7クリックで編集"),
                ev -> new NumericInputGUI(plugin, player, "Vertical", getVert.get(), 0.0, 50.0, setVert).open());
        setItem(15, item(Material.STONE_BUTTON, "§aHorizontal: §f" + getHoriz.get(), "§7クリックで編集"),
                ev -> new NumericInputGUI(plugin, player, "Horizontal", getHoriz.get(), 0.0, 50.0, setHoriz).open());

        setItem(22, item(Material.ANVIL, "§aPreset: Low", "§7Low kick"), ev -> { setVert.accept(1.5); setHoriz.accept(0.4); redraw(); });
        setItem(23, item(Material.IRON_BLOCK, "§aPreset: Strong", "§7Big kick"), ev -> { setVert.accept(8.0); setHoriz.accept(1.5); redraw(); });

        addBackButton(26, () -> player.closeInventory());
    }
}
