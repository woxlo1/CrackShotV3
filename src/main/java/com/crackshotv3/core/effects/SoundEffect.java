package com.crackshotv3.core.effects;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect implements Effect {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundEffect(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    @Override
    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }
}
