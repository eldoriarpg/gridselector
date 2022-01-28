/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.eldoutilities.utils.EMath;
import de.eldoria.gridselector.util.Colors;
import org.bukkit.Material;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Optional;

public class GridCluster {
    private int id = -1;
    private BoundingBox boundingBox;
    private int elementSize = 7;
    private int offset = 1;
    private int rows = 2;
    private int columns = 2;
    private Material borderMaterial = Material.BLUE_WOOL;
    private Material offsetMaterial = Material.LIGHT_GRAY_WOOL;
    private Material floorMaterial = Material.WHITE_WOOL;

    public GridCluster() {
    }

    public GridCluster(BoundingBox boundings, int elementSize, int offset, int rows, int columns,
                       Material borderMaterial, Material offsetMaterial, Material floorMaterial) {
        boundingBox = boundings;
        this.elementSize = elementSize;
        this.offset = offset;
        this.rows = rows;
        this.columns = columns;
        this.borderMaterial = borderMaterial;
        this.offsetMaterial = offsetMaterial;
        this.floorMaterial = floorMaterial;
    }

    public BlockState getBlock(Location location) {
        return BukkitAdapter.adapt(getMaterial(location).createBlockData());
    }

    public Material getMaterial(Location location) {
        return getMaterial(location.toVector());
    }

    public Material getMaterial(Vector3 location) {
        var optRegion = getRegion(location);
        if (optRegion.isEmpty()) return offsetMaterial;
        var region = optRegion.get();

        var shrink = shrink(region, -1);

        if (shrink.contains(toVector(location))) {
            return floorMaterial;
        }

        return borderMaterial;
    }

    private BoundingBox shrink(BoundingBox shrink, double amount) {
        return shrink.clone().expand(amount, 0, amount);
    }

    public Optional<BoundingBox> getRegion(Location location) {
        return getRegion(location.toVector());
    }

    public Optional<BoundingBox> getRegion(Vector3 vector) {
        var bukkitVec = toVector(vector);
        if (!boundingBox.contains(bukkitVec)) {
            return Optional.empty();
        }

        var totalElementSize = elementSize + 2 + offset;
        var min = toVector(boundingBox.getMin());

        var xOffset = EMath.diff(min.getX(), vector.getX());
        var zOffset = EMath.diff(min.getZ(), vector.getZ());

        var minOffset = Vector3.at(xOffset, 0, zOffset);

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

    private Vector toVector(Vector3 vector3) {
        return new Vector(vector3.getX(), 0, vector3.getZ());
    }

    private Vector3 toVector(Vector vector) {
        return Vector3.at(vector.getX(), vector.getY(), vector.getZ());
    }

    public BoundingBox getRegion(Vector3 base, int x, int z) {
        var totalElementSize = elementSize + 2 + offset;
        var min = new BlockVector(base.getX() + x * totalElementSize, 0, base.getZ() + z * totalElementSize);
        return BoundingBox.of(min, min.clone().add(new BlockVector(elementSize + 1.9, 1, elementSize + 1.9)));
    }

    public BoundingBox boundingBox() {
        return boundingBox;
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

    public boolean contains(org.bukkit.Location location) {
        return boundingBox.contains(location.toVector());
    }

    public static class Builder {
        World world;
        Location center;
        Direction direction;
        int elementSize = 7;
        int offset = 1;
        int rows = 2;
        int columns = 2;
        boolean expandRight = true;
        Material borderMaterial = Material.BLUE_WOOL;
        Material offsetMaterial = Material.LIGHT_GRAY_WOOL;
        Material floorMaterial = Material.WHITE_WOOL;

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

            var boundings = BoundingBox.of(floorVector(first), floorVector(second));
            return new GridCluster(boundings, elementSize, offset, rows, columns, borderMaterial, offsetMaterial, floorMaterial);
        }

        public BlockVector floorVector(Vector vector) {
            return new BlockVector(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
        }

        public String asComponent() {
            String message = MessageComposer.create()
                    .text("<%s>Cluster Settings", Colors.HEADING).newLine()
                    .text("<%s>Location: <%s>%s|%s", Colors.NAME, Colors.VALUE, center.getBlockX(), center.getBlockZ())
                    .space()
                    .text("<%s><click:run_command:'/sbrg grid cluster modify location'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Direction: <%s>%s", Colors.NAME, Colors.VALUE, direction.name()).space()
                    .text("<%s><click:run_command:'/sbrg grid cluster modify direction'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Expand: <%s>%s", Colors.NAME, Colors.VALUE, expandRight ? "right" : "left").space()
                    .text("<%s><click:run_command:'/sbrg grid cluster modify expandRight'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Rows: <%s>%s", Colors.NAME, Colors.VALUE, rows).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify rows '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Columns: <%s>%s", Colors.NAME, Colors.VALUE, columns).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify colums '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Offset: <%s>%s", Colors.NAME, Colors.VALUE, offset).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify offsetMaterial'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Floor Material: <%s>%s", Colors.NAME, Colors.VALUE, floorMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify floorMaterial'>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Border Material: <%s>%s", Colors.NAME, Colors.VALUE, borderMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify borderMaterial '>[change]</click>", Colors.CHANGE)
                    .newLine()
                    .text("<%s>Offset Material: <%s>%s", Colors.NAME, Colors.VALUE, offsetMaterial).space()
                    .text("<%s><click:suggest_command:'/sbrg grid cluster modify offsetMaterial '>[change]</click>", Colors.CHANGE)
                    .build();
            return message;
        }

        private static Direction rotate(Direction direction, boolean rotateRight) {
            var index = direction.toRotationIndex().getAsInt() + (rotateRight ? 4 : 12);
            return Direction.fromRotationIndex(index % 16).get();
        }

        public Location center() {
            return center;
        }

        public Direction direction() {
            return direction;
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

        public boolean isExpandRight() {
            return expandRight;
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

        public void center(Location center) {
            this.world = (World) center.getExtent();
            this.center = center.setY(world.getMinY());
        }

        public void direction(Direction direction) {
            this.direction = direction;
        }
    }
}
