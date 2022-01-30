/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;

import java.util.List;
import java.util.Optional;

public class WorldAdapter {
    private final List<RegionAdapter> adapters;

    public WorldAdapter(List<RegionAdapter> adapters) {
        this.adapters = adapters;
    }

    /**
     * Get a schematicRegion from the first schematicRegion where {@link RegionAdapter#isApplicable(org.bukkit.Location)} returns true.
     * @param location location to check
     * @return schematicRegion results if the application schematicRegion adapter returns a schematicRegion
     */
    public Optional<RegionResult> getRegion(Location location) {
        var loc = BukkitAdapter.adapt(location);
        return adapters.stream()
                .filter(a -> a.isApplicable(loc))
                .findFirst()
                .flatMap(adaper -> adaper.getRegion(loc));
    }
}
