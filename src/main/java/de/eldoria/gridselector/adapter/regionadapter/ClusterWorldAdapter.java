/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import de.eldoria.gridselector.config.Configuration;
import org.bukkit.Location;

import java.util.Optional;

public class ClusterWorldAdapter implements RegionAdapter {
    private final Configuration configuration;

    public ClusterWorldAdapter(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isApplicable(Location location) {
        return !configuration.getClusterWorld(location.getWorld()).cluster().isEmpty();
    }

    @Override
    public Optional<RegionResult> getRegion(Location location) {
        return configuration.getClusterWorld(location.getWorld()).getCluster(location)
                .flatMap(p -> p.getRegion(BukkitAdapter.adapt(location)))
                .map(plot -> new RegionResult(plot.id(), plot.as2DRegion(location.getBlockY())));

    }
}
