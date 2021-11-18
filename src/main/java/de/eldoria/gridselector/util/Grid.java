/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.util;

import de.eldoria.eldoutilities.container.Pair;
import org.bukkit.util.Vector;

import static java.lang.Math.floor;

public final class Grid {
    private Grid() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static Pair<Integer, Integer> calcGridCoord(int size, double x, double z) {
        var gridX = (int) floor(floor(x) / size);
        var gridZ = (int) floor(floor(z) / size);
        return Pair.of(gridX, gridZ);
    }

    public static Pair<Vector, Vector> calcGridBoundaries(int size, int x, int z) {
        var x1 = x * size;
        var z1 = z * size;

        return Pair.of(new Vector(x1, 0, z1), new Vector(x1 + size - 1, 0, z1 + size - 1));
    }

    public static Pair<Vector, Vector> calcGridBoundaries(int size, Pair<Integer, Integer> gridCoord) {
        return calcGridBoundaries(size, gridCoord.first, gridCoord.second);
    }
}
