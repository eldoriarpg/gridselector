/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import com.sk89q.worldedit.math.Vector3;
import org.bukkit.Material;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridClusterTest {

    private GridCluster pCluster;
    private GridCluster nCluster;
    private final Material border = Material.RED_WOOL;
    private final Material offset = Material.LIGHT_GRAY_WOOL;
    private final Material floor = Material.WHITE_WOOL;

    @BeforeEach
    void setUp() {
        pCluster = new GridCluster(BoundingBox.of(new Vector(0, 0, 0), new Vector(17, 256, 17)), 3, 1, 3, 3,
                border, offset, floor);
        nCluster = new GridCluster(BoundingBox.of(new Vector(-17, 0, -17), new Vector(0, 256, 0)), 3, 1, 3, 3,
                border, offset, floor);
    }

    @Test
    void getRegion() {
        // Border
        var region = pCluster.getRegion(Vector3.at(0, 0, 0));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(0, 0, 0), new Vector(4, 0, 4)), region.get());

        // Border
        region = pCluster.getRegion(Vector3.at(4, 0, 4));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(0, 0, 0), new Vector(4, 0, 4)), region.get());

        // Offset
        region = pCluster.getRegion(Vector3.at(5, 0, 5));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = pCluster.getRegion(Vector3.at(6, 0, 6));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(6, 0, 6), new Vector(10, 0, 10)), region.get());

        // Border
        region = pCluster.getRegion(Vector3.at(10, 0, 10));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(6, 0, 6), new Vector(10, 0, 10)), region.get());

        // Offset
        region = pCluster.getRegion(Vector3.at(11, 0, 11));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(Vector3.at(-17, 0, -17));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-17, 0, -17), new Vector(-13, 0, -13)), region.get());

        // Border
        region = nCluster.getRegion(Vector3.at(-13, 0, -13));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-17, 0, -17), new Vector(-13, 0, -13)), region.get());

        // Offset
        region = nCluster.getRegion(Vector3.at(-12, 0, -12));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(Vector3.at(-11, 0, -11));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-11, 0, -11), new Vector(-7, 0, -7)), region.get());

        // Border
        region = nCluster.getRegion(Vector3.at(-7, 0, -7));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-11, 0, -11), new Vector(-7, 0, -7)), region.get());

        // Offset
        region = nCluster.getRegion(Vector3.at(-6, 0, -6));
        Assertions.assertTrue(region.isEmpty());

        // Border
        region = nCluster.getRegion(Vector3.at(-5, 0, -5));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-5, 0, -5), new Vector(-1, 0, -1)), region.get());

        // Border
        region = nCluster.getRegion(Vector3.at(-1, 0, -1));
        Assertions.assertTrue(region.isPresent());
        Assertions.assertEquals(BoundingBox.of(new Vector(-5, 0, -5), new Vector(-1, 0, -1)), region.get());

        // Offset
        region = nCluster.getRegion(Vector3.at(0, 0, 0));
        Assertions.assertTrue(region.isEmpty());
    }

    @Test
    void getMaterial() {
        // Border
        var material = pCluster.getMaterial(Vector3.at(0, 0, 0));
        Assertions.assertEquals(border, material);

        // Floor
        material = pCluster.getMaterial(Vector3.at(1, 0, 1));
        Assertions.assertEquals(floor, material);

        // Floor
        material = pCluster.getMaterial(Vector3.at(3, 0, 3));
        Assertions.assertEquals(floor, material);

        // Border
        material = pCluster.getMaterial(Vector3.at(4, 0, 4));
        Assertions.assertEquals(border, material);

        // Offset
        material = pCluster.getMaterial(Vector3.at(5, 0, 5));
        Assertions.assertEquals(material, offset);

        // Border
        material = pCluster.getMaterial(Vector3.at(6, 0, 6));
        Assertions.assertEquals(border, material);

        // Floor
        material = pCluster.getMaterial(Vector3.at(7, 0, 7));
        Assertions.assertEquals(floor, material);

        // Floor
        material = pCluster.getMaterial(Vector3.at(9, 0, 9));
        Assertions.assertEquals(floor, material);

        // Border
        material = pCluster.getMaterial(Vector3.at(10, 0, 10));
        Assertions.assertEquals(border, material);

        // Offset
        material = pCluster.getMaterial(Vector3.at(11, 0, 11));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(Vector3.at(-17, 0, -17));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-16, 0, -16));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-14, 0, -14));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(Vector3.at(-13, 0, -13));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(Vector3.at(-12, 0, -12));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(Vector3.at(-11, 0, -11));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-10, 0, -10));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-8, 0, -8));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(Vector3.at(-7, 0, -7));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(Vector3.at(-6, 0, -6));
        Assertions.assertEquals(material, offset);

        // Border
        material = nCluster.getMaterial(Vector3.at(-5, 0, -5));
        Assertions.assertEquals(border, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-4, 0, -4));
        Assertions.assertEquals(floor, material);

        // Floor
        material = nCluster.getMaterial(Vector3.at(-2, 0, -2));
        Assertions.assertEquals(floor, material);

        // Border
        material = nCluster.getMaterial(Vector3.at(-1, 0, -1));
        Assertions.assertEquals(border, material);

        // Offset
        material = nCluster.getMaterial(Vector3.at(0, 0, 0));
        Assertions.assertEquals(material, offset);
    }
}
