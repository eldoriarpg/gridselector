/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.util;

import de.eldoria.eldoutilities.container.Pair;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GridTest {

    @Test
    void calcGridCoord() {
        // Simple positive tests
        Assertions.assertEquals(Pair.of(0, 0), Grid.calcGridCoord(10, 0, 0));
        Assertions.assertEquals(Pair.of(0, 0), Grid.calcGridCoord(10, 9, 9));

        Assertions.assertEquals(Pair.of(1, 1), Grid.calcGridCoord(10, 10, 10));
        Assertions.assertEquals(Pair.of(1, 1), Grid.calcGridCoord(10, 19, 19));

        // Simple negative tests
        Assertions.assertEquals(Pair.of(-1, -1), Grid.calcGridCoord(10, -1, -1));
        Assertions.assertEquals(Pair.of(-1, -1), Grid.calcGridCoord(10, -10, -10));

        Assertions.assertEquals(Pair.of(-2, -2), Grid.calcGridCoord(10, -11, -11));
        Assertions.assertEquals(Pair.of(-2, -2), Grid.calcGridCoord(10, -20, -20));

        // Simple mixed tests
        Assertions.assertEquals(Pair.of(-1, 0), Grid.calcGridCoord(10, -1, 0));
        Assertions.assertEquals(Pair.of(-1, 0), Grid.calcGridCoord(10, -10, 9));

        Assertions.assertEquals(Pair.of(0, -1), Grid.calcGridCoord(10, 0, -1));
        Assertions.assertEquals(Pair.of(0, -1), Grid.calcGridCoord(10, 9, -10));
    }

    @Test
    void getGridBoundaries() {
        Assertions.assertEquals(Pair.of(vec(0,0), vec(9,9)), Grid.calcGridBoundaries(10, 0,0));
        Assertions.assertEquals(Pair.of(vec(10,10), vec(19,19)), Grid.calcGridBoundaries(10, 1,1));

        Assertions.assertEquals(Pair.of(vec(-10,-10), vec(-1,-1)), Grid.calcGridBoundaries(10, -1,-1));
        Assertions.assertEquals(Pair.of(vec(-20,-10), vec(-11,-1)), Grid.calcGridBoundaries(10, -2,-1));

        Assertions.assertEquals(Pair.of(vec(-10,0), vec(-1,9)), Grid.calcGridBoundaries(10, -1,0));
    }

    private static Vector vec(int x, int z) {
        return new Vector(x, 0, z);
    }
}
