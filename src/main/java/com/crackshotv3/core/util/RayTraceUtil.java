package com.crackshotv3.core.util;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;

public class RayTraceUtil {

    /**
     * エンティティ優先のレイトレース
     */
    public static RayTraceResult trace(World world, Location from, Vector direction, double maxDistance) {
        return world.rayTrace(
                from,
                direction,
                maxDistance,
                FluidCollisionMode.NEVER,
                true,
                0.2,
                (entity) -> entity instanceof LivingEntity
        );
    }

    /**
     * 特定エンティティを除外
     */
    public static RayTraceResult traceExclude(World world, Location from, Vector direction, double maxDistance, Entity exclude) {
        return world.rayTrace(
                from,
                direction,
                maxDistance,
                FluidCollisionMode.NEVER,
                true,
                0.2,
                (entity) -> entity instanceof LivingEntity && !entity.equals(exclude)
        );
    }
}
