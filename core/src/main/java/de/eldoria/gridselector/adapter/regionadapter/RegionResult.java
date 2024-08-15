/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.regions.CuboidRegion;

public record RegionResult(String identifier, CuboidRegion region, CuboidRegion walls, int worldHeight) {

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
