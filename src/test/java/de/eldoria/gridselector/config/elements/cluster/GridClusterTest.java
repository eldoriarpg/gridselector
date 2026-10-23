/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements.cluster;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.NullWorld;
import org.bukkit.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GridClusterTest {

    private GridCluster pCluster;
    private GridCluster nCluster;
    private final Material border = Material.RED_WOOL;
    private final Material offset = Material.LIGHT_GRAY_WOOL;
    private final Material floor = Material.WHITE_WOOL;

    @BeforeEach
    void setUp() {
        pCluster = new GridCluster(Plot.of(0, 0, 17, 17), 3, 1, 3, 3, border, offset, floor);
        nCluster = new GridCluster(Plot.of(-17, -17, 0, 0), 3, 1, 3, 3, border, offset, floor);
    }

    @Test
    void getRegion() {
        var p1 = Plot.of(0, 0, 4, 4);
        var p2 = Plot.of(6, 6, 10, 10);
        var n1 = Plot.of(-17, -17, -13, -13);
        var n2 = Plot.of(-11, -11, -7, -7);
        var n3 = Plot.of(-5, -5, -1, -1);

        // Border
        var region = pCluster.getRegion(BlockVector2.at(0, 0));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(p1, region.get());

        // Border
        region = pCluster.getRegion(BlockVector2.at(4, 4));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(p1, region.get());

        // Offset
        region = pCluster.getRegion(BlockVector2.at(5, 5));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = pCluster.getRegion(BlockVector2.at(6, 6));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(p2, region.get());

        // Border
        region = pCluster.getRegion(BlockVector2.at(10, 10));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(p2, region.get());

        // Offset
        region = pCluster.getRegion(BlockVector2.at(11, 11));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-17, -17));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n1, region.get());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-13, -13));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n1, region.get());

        // Offset
        region = nCluster.getRegion(BlockVector2.at(-12, -12));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-11, -11));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n2, region.get());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-7, -7));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n2, region.get());

        // Offset
        region = nCluster.getRegion(BlockVector2.at(-6, -6));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-5, -5));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n3, region.get());

        // Border
        region = nCluster.getRegion(BlockVector2.at(-1, -1));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(n3, region.get());

        // Offset
        region = nCluster.getRegion(BlockVector2.at(0, 0));
        Assertions.assertTrue(region.isEmpty());
    }

    @Test
    void getMaterial() {
        // Border
        var material = pCluster.getMaterial(BlockVector2.at(0, 0));
        Assertions.assertEquals(border, material);

        // Floor
        material = pCluster.getMaterial(BlockVector2.at(1, 1));
        Assertions.assertEquals(floor, material);

        // Floor
        material = pCluster.getMaterial(BlockVector2.at(3, 3));
        Assertions.assertEquals(floor, material);

        // Border
        material = pCluster.getMaterial(BlockVector2.at(4, 4));
        Assertions.assertEquals(border, material);

        // Offset
        material = pCluster.getMaterial(BlockVector2.at(5, 5));
        Assertions.assertEquals(material, offset);

        // Border
        material = pCluster.getMaterial(BlockVector2.at(6, 6));
        Assertions.assertEquals(border, material);

        // Floor
        material = pCluster.getMaterial(BlockVector2.at(7, 7));
        Assertions.assertEquals(floor, material);

        // Floor
        material = pCluster.getMaterial(BlockVector2.at(9, 9));
        Assertions.assertEquals(floor, material);

        // Border
        material = pCluster.getMaterial(BlockVector2.at(10, 10));
        Assertions.assertEquals(border, material);

        // Offset
        material = pCluster.getMaterial(BlockVector2.at(11, 11));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-17, -17));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-16, -16));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-14, -14));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-13, -13));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(BlockVector2.at(-12, -12));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-11, -11));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-10, -10));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-8, -8));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-7, -7));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(BlockVector2.at(-6, -6));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-5, -5));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-4, -4));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(BlockVector2.at(-2, -2));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(BlockVector2.at(-1, -1));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(BlockVector2.at(0, 0));
        Assertions.assertEquals(material, offset);
    }

    @Test
    void getRegions() {
        var regions = pCluster.getRegions();
        Assertions.assertEquals(9, regions.size());
        var elementSize = 20;
        var offsetSize = 1;
        var fullSize = elementSize + offsetSize + 2;
        var large = new GridCluster(
                Plot.of(0, 0, fullSize * 30, fullSize * 30), elementSize, offsetSize, 30, 30, border, offset, floor);
        regions = large.getRegions();
        Assertions.assertEquals(30 * 30, regions.size());
        large = new GridCluster(
                Plot.of(0, 0, fullSize * 200, fullSize * 200),
                elementSize,
                offsetSize,
                200,
                200,
                border,
                offset,
                floor);
        regions = large.getRegions();
        Assertions.assertEquals(200 * 200, regions.size());
    }

    @ParameterizedTest
    @CsvSource({
        "SOUTH, true, -18, 0, 0, 18",
        "SOUTH, false, 0, 0, 18, 18",
        "NORTH, true, 0, -18, 18, 0",
        "NORTH, false, -18, -18, 0, 0",
        "EAST, true, 0, 0, 18, 18",
        "EAST, false, 0, -18, 18, 0",
        "WEST, true, -18, -18, 0, 0",
        "WEST, false, -18, 0, 0, 18"
    })
    void buildStartsOnCenterBlock(Direction direction, boolean expandRight, int minX, int minZ, int maxX, int maxZ) {
        var builder = GridCluster.builder(new Location(new FlatWorld(), Vector3.at(0.5, 64, 0.5)), direction);
        builder.expandRight(expandRight);

        var cluster = builder.build();

        Assertions.assertEquals(Plot.of(minX, minZ, maxX, maxZ), cluster.plot());
        Assertions.assertEquals(4, cluster.getRegions().size());
    }

    private static final class FlatWorld extends NullWorld {
        @Override
        public int getMinY() {
            return -64;
        }

        @Override
        public int getMaxY() {
            return 319;
        }
    }
}
