/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements.cluster;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Plot implements ConfigurationSerializable {
    private final BlockVector2 min;
    private final BlockVector2 max;

    public Plot(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        min = fromParserString(map.getValue("min"));
        max = fromParserString(map.getValue("max"));
    }

    private Plot(BlockVector2 min, BlockVector2 max) {
        this.min = BlockVector2.at(Math.min(min.getBlockX(), max.getBlockX()), Math.min(min.getBlockZ(), max.getBlockZ()));
        this.max = BlockVector2.at(Math.max(min.getBlockX(), max.getBlockX()), Math.max(min.getBlockZ(), max.getBlockZ()));
    }

    public static Plot of(int minX, int minZ, int maxX, int maxZ) {
        return new Plot(BlockVector2.at(minX, minZ), BlockVector2.at(maxX, maxZ));
    }

    public static Plot of(BlockVector2 min, BlockVector2 max) {
        return new Plot(min, max);
    }

    private BlockVector2 fromParserString(String val) {
        return BlockVector2.at(Integer.parseInt(val.split(",")[0]), Integer.parseInt(val.split(",")[1]));
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("min", min.toParserString())
                .add("max", max.toParserString())
                .build();
    }

    /**
     * Expands the plot by the block count into all directions.
     *
     * @param blocks blocks
     * @return a new plot
     */
    public Plot expand(int blocks) {
        return Plot.of(min.subtract(blocks, blocks), max.add(blocks, blocks));
    }

    /**
     * Returns the plot borderless. Equal to calling {@link #expand(int)} with -1.
     *
     * @return the plot without border
     */
    public Plot borderLessPlot() {
        return expand(-1);
    }

    /**
     * Checks if the plot contains the location. Upper and lower bound inclusive.
     *
     * @param vector2 vector2
     * @return true if the vector is part of the plot.
     */
    public boolean contains(BlockVector2 vector2) {
        return min.getBlockX() <= vector2.getBlockX() && vector2.getBlockX() <= max.getBlockX()
               && min.getBlockZ() <= vector2.getBlockZ() && vector2.getBlockZ() <= max.getBlockZ();
    }

    public BlockVector2 min() {
        return min;
    }

    public BlockVector2 max() {
        return max;
    }

    /**
     * Checks if the plot overlaps with another plot
     *
     * @param plot another plot
     * @return true if they overlap
     */
    public boolean overlaps(Plot plot) {
        return min.getBlockX() < plot.max().getBlockX() && max.getBlockX() > plot.min().getBlockX()
               && min.getBlockZ() < plot.max().getBlockZ() && max.getBlockZ() > plot.min().getBlockZ();

    }

    /**
     * Get the plot as a cuboid region with y as 0
     *
     * @return cuboid region
     */
    public CuboidRegion as2DRegion() {
        return as2DRegion(0);
    }

    public CuboidRegion as2DRegion(int y) {
        return new CuboidRegion(min.toBlockVector3(y), max.toBlockVector3(y));
    }

    @Override
    public String toString() {
        return "Plot{" +
               "min=" + min +
               ", max=" + max +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plot)) return false;

        Plot plot = (Plot) o;

        if (!min.equals(plot.min)) return false;
        return max.equals(plot.max);
    }

    @Override
    public int hashCode() {
        int result = min.hashCode();
        result = 31 * result + max.hashCode();
        return result;
    }

    public String id() {
        return min.toParserString();
    }

    public CuboidRegion asRegion(World world) {
        return new CuboidRegion(min.toBlockVector3(world.getMinY()), max.toBlockVector3(world.getMaxY()));
    }
}
