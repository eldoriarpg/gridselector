/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MarkerResult {

    String identifier;
    CuboidRegion region;
    private final CuboidRegion fullRegion;
    private int yMin;

    public MarkerResult(String identifier, CuboidRegion region, CuboidRegion fullRegion, int minHeight) {
        this.identifier = identifier;
        this.region = region;
        this.fullRegion = fullRegion;
        this.yMin = minHeight;
    }

    public void yMin(int yMin) {
        this.yMin = yMin;
    }

    public String identifier() {
        return identifier;
    }

    public CuboidRegion region() {
        return region;
    }

    public int yMin() {
        return yMin;
    }

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
        region.setPos1(region.getMinimumPoint().withY(yMin).add(-1,0,-1));
        region.setPos2(region.getMaximumPoint().withY(yMin).add(1,0,1));
        return region;
    }

    public List<Vector> getBorderBlocks() {
        var border = getBorder();
        var min = border.getMinimumPoint();
        var max = border.getMaximumPoint();

        List<Vector> blocks = new ArrayList<>();

        for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
            blocks.add(new Vector(x, yMin, min.getBlockZ()));
            blocks.add(new Vector(x, yMin, max.getBlockZ()));
        }

        for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
            blocks.add(new Vector(min.getBlockX(), yMin, z));
            blocks.add(new Vector(max.getBlockX(), yMin, z));
        }
        return blocks;
    }
}
