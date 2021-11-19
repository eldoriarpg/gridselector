/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import de.eldoria.gridselector.config.Configuration;
import org.bukkit.Location;

import java.util.Optional;

public class GridWorldAdapter implements RegionAdapter {
    private final Configuration configuration;

    public GridWorldAdapter(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isApplicable(Location location) {
        return configuration.getGridWorld(location).isPresent();
    }

    @Override
    public Optional<RegionResult> getRegion(Location location) {
        return configuration.getGridWorld(location)
                .map(world -> new RegionResult(world.getGridItentifier(location), world.getGridElement(location)));
    }
}
