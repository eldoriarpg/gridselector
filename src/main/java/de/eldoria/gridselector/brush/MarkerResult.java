/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.brush;

import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record MarkerResult(String identifier, CuboidRegion region, CuboidRegion fullRegion, int minHeight) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkerResult)) return false;

        var that = (MarkerResult) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    public List<Vector> getCorners() {
        List<Vector> result = new ArrayList<>();
        var max = region.getMaximumPoint();
        var min = region.getMinimumPoint();
        for (var x : new int[]{min.getX() - 1, max.getX() + 1}) {
            for (var y : new int[]{min.getY() - 1, max.getY() + 1}) {
                for (var z : new int[]{min.getZ() - 1, max.getZ() + 1}) {
                    result.add(new Vector(x, y, z));
                }
            }
        }
        return result;
    }

    public CuboidRegion getBorder() {
        var region = fullRegion.clone();
        region.setPos1(region.getMinimumPoint().withY(minHeight).add(-1, 0, -1));
        region.setPos2(region.getMaximumPoint().withY(minHeight).add(1, 0, 1));
        return region;
    }

    public Set<Vector> getBorderBlocks() {
        var border = getBorder();
        var min = border.getMinimumPoint();
        var max = border.getMaximumPoint();

        Set<Vector> blocks = new HashSet<>();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            blocks.add(new Vector(x, minHeight, min.getBlockZ()));
            blocks.add(new Vector(x, minHeight, max.getBlockZ()));
        }

        for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
            blocks.add(new Vector(min.getBlockX(), minHeight, z));
            blocks.add(new Vector(max.getBlockX(), minHeight, z));
        }
        return blocks;
    }
}
