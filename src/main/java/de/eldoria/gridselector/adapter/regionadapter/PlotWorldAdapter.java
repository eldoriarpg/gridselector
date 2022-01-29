/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.world.PlotAreaManager;
import org.bukkit.Location;

import java.util.Optional;

public class PlotWorldAdapter implements RegionAdapter {
    private final PlotAreaManager manager;

    public PlotWorldAdapter(PlotSquared plotSquared) {
        manager = plotSquared.getPlotAreaManager();
    }

    @Override
    public boolean isApplicable(Location location) {
        var loc = BukkitUtil.adapt(location);
        return manager.hasPlotArea(loc.getWorldName());
    }

    @Override
    public Optional<RegionResult> getRegion(Location location) {
        var loc = BukkitUtil.adapt(location);
        return Optional.ofNullable(manager.getPlotArea(loc))
                .map(area -> area.getPlot(loc))
                .map(p -> new RegionResult(p.getId().toString(), p.getLargestRegion()));
    }
}
