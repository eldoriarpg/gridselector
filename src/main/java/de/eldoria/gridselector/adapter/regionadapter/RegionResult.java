/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.regions.CuboidRegion;

import java.util.OptionalInt;

/**
 * A plot that can be selected.
 *
 * @param identifier identifies the plot within its world
 * @param region     the inside of the plot, without border or walls, over the full world height
 * @param walls      the plot including its border or walls
 * @param floor      the height of the plot's top floor layer, if the plot's world defines one
 */
public record RegionResult(String identifier, CuboidRegion region, CuboidRegion walls, OptionalInt floor) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionResult)) return false;

        var that = (RegionResult) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
