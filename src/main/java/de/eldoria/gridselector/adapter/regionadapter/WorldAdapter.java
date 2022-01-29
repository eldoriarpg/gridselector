/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorldAdapter {
    private final List<RegionAdapter> adapters;

    public WorldAdapter(List<RegionAdapter> adapters) {
        this.adapters = adapters;
    }

    public Optional<RegionResult> getRegion(Location location) {
        var loc = BukkitAdapter.adapt(location);
        return adapters.stream()
                .filter(a -> a.isApplicable(loc))
                .findFirst()
                .flatMap(adaper -> adaper.getRegion(loc));
    }
}
