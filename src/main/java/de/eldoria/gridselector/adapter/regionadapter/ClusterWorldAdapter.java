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
        return !configuration.cluster().world(location.getWorld()).cluster().isEmpty();
    }

    @Override
    public Optional<RegionResult> getRegion(Location location) {
        return configuration.cluster().world(location.getWorld()).getCluster(location)
                .flatMap(cluster -> {
                    var optPlot = cluster.getRegion(BukkitAdapter.adapt(location));
                    return optPlot.map(plot -> new RegionResult(plot.id(),
                            plot.borderLessPlot().asRegion(BukkitAdapter.adapt(location.getWorld())),
                            plot.asRegion(BukkitAdapter.adapt(location.getWorld())), cluster.minHeight()));
                });

    }
}
