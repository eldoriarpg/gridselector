/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements.cluster;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import de.eldoria.eldoutilities.utils.EMath;
import de.eldoria.eldoutilities.utils.EnumUtil;
import de.eldoria.gridselector.util.Colors;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class GridCluster implements ConfigurationSerializable {
    private int id = -1;
    private int minHeight = 0;
    private Plot plot;
    private int elementSize = 7;
    private int offset = 1;
    private int rows = 2;
    private int columns = 2;
    private Material borderMaterial = Material.RED_CONCRETE;
    private Material offsetMaterial = Material.LIGHT_GRAY_CONCRETE;
    private Material floorMaterial = Material.WHITE_CONCRETE;

    public GridCluster(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        id = map.getValue("id");
        minHeight = map.getValue("minHeight");
        plot = map.getValue("plot");
        elementSize = map.getValue("elementSize");
        offset = map.getValue("offset");
        rows = map.getValue("rows");
        columns = map.getValue("columns");
        borderMaterial = map.getValue("borderMaterial", e -> EnumUtil.parse(e, Material.class).orElse(borderMaterial));
        offsetMaterial = map.getValue("offsetMaterial", e -> EnumUtil.parse(e, Material.class).orElse(offsetMaterial));
        floorMaterial = map.getValue("floorMaterial", e -> EnumUtil.parse(e, Material.class).orElse(floorMaterial));
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("id", id)
                .add("minHeight", minHeight)
                .add("plot", plot)
                .add("elementSize", elementSize)
                .add("offset", offset)
                .add("rows", rows)
                .add("columns", columns)
                .add("borderMaterial", borderMaterial)
                .add("offsetMaterial", offsetMaterial)
                .add("floorMaterial", floorMaterial)
                .build();
    }


    public GridCluster() {
    }

    public void updateMinHeight(int minHeight){
        this.minHeight = minHeight;
    }

    public int minHeight() {
        return minHeight;
    }

    public GridCluster(Plot boundings, int elementSize, int offset, int rows, int columns,
                       Material borderMaterial, Material offsetMaterial, Material floorMaterial) {
        plot = boundings;
        this.elementSize = elementSize;
        this.offset = offset;
        this.rows = rows;
        this.columns = columns;
        this.borderMaterial = borderMaterial;
        this.offsetMaterial = offsetMaterial;
        this.floorMaterial = floorMaterial;
    }

    public BlockState getBlock(BlockVector2 location) {
        return BukkitAdapter.adapt(getMaterial(location).createBlockData());
    }

    public Material getMaterial(Location location) {
        return getMaterial(location.toVector().toBlockPoint().toBlockVector2());
    }

    public Material getMaterial(BlockVector2 location) {
        var optRegion = getRegion(location);
        if (optRegion.isEmpty()) return offsetMaterial;
        var region = optRegion.get();

        var borderless = region.borderLessPlot();

        if (borderless.contains(location)) {
            return floorMaterial;
        }

        return borderMaterial;
    }

    private BoundingBox shrink(BoundingBox shrink, double amount) {
        return shrink.clone().expand(amount, 0, amount);
    }

    public Optional<Plot> getRegion(Location location) {
        return getRegion(location.toVector().toVector2().toBlockPoint());
    }

    public Optional<Plot> getRegion(BlockVector2 vector) {
        if (!plot.contains(vector)) {
            return Optional.empty();
        }

        var totalElementSize = elementSize + 2 + offset;
        var min = plot.min();

        var xOffset = EMath.diff(min.getX(), vector.getX());
        var zOffset = EMath.diff(min.getZ(), vector.getZ());

        var minOffset = BlockVector2.at(xOffset, zOffset);

        var xIndex = minOffset.getX() % totalElementSize;
        var zIndex = minOffset.getZ() % totalElementSize;

        if (xIndex >= elementSize + 2) {
            return Optional.empty();
        }

        if (zIndex >= elementSize + 2) {
            return Optional.empty();
        }

        var gridX = Math.floor(minOffset.getX() / totalElementSize);
        var gridZ = Math.floor(minOffset.getZ() / totalElementSize);

        return Optional.ofNullable(getRegion(min, (int) gridX, (int) gridZ));
    }

    private BlockVector2 toVector(Vector3 vector3) {
        return BlockVector2.at(vector3.getX(), vector3.getZ());
    }

    private BlockVector2 toVector(Vector vector) {
        return BlockVector2.at(vector.getX(), vector.getZ());
    }

    public Plot getRegion(BlockVector2 base, int x, int z) {
        var totalElementSize = elementSize + 2 + offset;
        var min = BlockVector2.at(base.getX() + x * totalElementSize, base.getZ() + z * totalElementSize);
        return Plot.of(min, min.add(BlockVector2.at(elementSize + 1, elementSize + 1)));
    }

    public Plot boundingBox() {
        return plot;
    }

    public int elementSize() {
        return elementSize;
    }

    public int offset() {
        return offset;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public Material borderMaterial() {
        return borderMaterial;
    }

    public Material offsetMaterial() {
        return offsetMaterial;
    }

    public Material floorMaterial() {
        return floorMaterial;
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("Id already set");
        }
        this.id = id;
    }

    public static Builder builder(Location center, Direction direction) {
        return new Builder(center, direction);
    }

    public boolean contains(BlockVector2 location) {
        return plot.contains(location);
    }

    public static class Builder {
        private World world;
        private Location center;
        private Direction direction;
        private int elementSize = 7;
        private int offset = 1;
        private int rows = 2;
        private int columns = 2;
        private boolean expandRight = true;
        private Material borderMaterial = Material.RED_CONCRETE;
        private Material offsetMaterial = Material.LIGHT_GRAY_CONCRETE;
        private Material floorMaterial = Material.WHITE_CONCRETE;

        public Builder(Location center, Direction direction) {
            this.world = (World) center.getExtent();
            this.center = center.setY(world.getMinY());
            this.direction = direction;
        }

        public void elementSize(int elementSize) {
            this.elementSize = elementSize;
        }

        public void offset(int offset) {
            this.offset = offset;
        }

        public void rows(int rows) {
            this.rows = rows;
        }

        public void columns(int columns) {
            this.columns = columns;
        }

        public void expandRight(boolean expandRight) {
            this.expandRight = expandRight;
        }

        public void borderMaterial(Material borderMaterial) {
            this.borderMaterial = borderMaterial;
        }

        public void offsetMaterial(Material offsetMaterial) {
            this.offsetMaterial = offsetMaterial;
        }

        public void floorMaterial(Material floorMaterial) {
            this.floorMaterial = floorMaterial;
        }

        public GridCluster build() {
            var rowsSize = (elementSize + 2) * rows + (rows - 1) * offset;
            var columnSize = (elementSize + 2) * columns + (columns - 1) * offset;
            var columnDir = rotate(direction, expandRight);

            var offsetVec = Vector3.ZERO.add(direction.toVector().multiply(rowsSize)).add(columnDir.toVector().multiply(columnSize));

            var first = BukkitAdapter.adapt(center).toVector().toBlockVector();
            var second = BukkitAdapter.adapt(new Location(world, center.toVector().add(offsetVec).withY(world.getMaxY()))).toVector().toBlockVector();

            var boundings = Plot.of(floorVector(first), floorVector(second));
            return new GridCluster(boundings, elementSize, offset, rows, columns, borderMaterial, offsetMaterial, floorMaterial);
        }

        public BlockVector2 floorVector(Vector vector) {
            return BlockVector2.at(vector.getBlockX(), vector.getBlockZ());
        }

        public String asComponent() {
            String message = MessageComposer.create()
                    .text("<%s>Cluster Settings", Colors.HEADING).newLine()
                    .text("<%s>Location: <%s>%s|%s", Colors.NAME, Colors.VALUE, center.getBlockX(), center.getBlockZ())
                    .space()
                    .text("<%s><click:run_command:'/sbrg cluster modify center'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Direction: <%s>%s", Colors.NAME, Colors.VALUE, direction.name()).space()
                    .text("<%s><click:run_command:'/sbrg cluster modify direction'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Expand: <%s>%s", Colors.NAME, Colors.VALUE, expandRight ? "right" : "left").space()
                    .text("<%s><click:run_command:'/sbrg cluster modify expandRight'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Size: <%s>%s", Colors.NAME, Colors.VALUE, elementSize).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify size '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Rows: <%s>%s", Colors.NAME, Colors.VALUE, rows).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify rows '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Columns: <%s>%s", Colors.NAME, Colors.VALUE, columns).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify columns '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Offset: <%s>%s", Colors.NAME, Colors.VALUE, offset).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify offset '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Floor Material: <%s>%s", Colors.NAME, Colors.VALUE, floorMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify floorMaterial '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Border Material: <%s>%s", Colors.NAME, Colors.VALUE, borderMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify borderMaterial '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Offset Material: <%s>%s", Colors.NAME, Colors.VALUE, offsetMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg cluster modify offsetMaterial '>[change]</click>", Colors.CHANGE)
                    .build();
            return message;
        }

        private static Direction rotate(Direction direction, boolean rotateRight) {
            var index = direction.toRotationIndex().getAsInt() + (rotateRight ? 4 : 12);
            return Direction.fromRotationIndex(index % 16).get();
        }

        public boolean isExpandRight() {
            return expandRight;
        }

        public void center(Location center) {
            this.world = (World) center.getExtent();
            this.center = center.setY(world.getMinY());
        }

        public void direction(Direction direction) {
            this.direction = direction;
        }
    }
}
