/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.generator.ClassicPlotWorld;
import com.plotsquared.core.plot.world.PlotAreaManager;
import com.sk89q.worldedit.math.BlockVector3;
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
                .map(area -> {

                    var p = area.getPlot(loc);
                    if (p == null) {
                        return null;
                    }
                    var walls = p.getLargestRegion().clone();
                    walls.expand(BlockVector3.at(1, 0, 1));
                    //TODO: Add plot world height if possible;
                    var minHeight = p.getLargestRegion().getMinimumY();
                    if(area instanceof ClassicPlotWorld world){
                        minHeight = world.PLOT_HEIGHT;
                    }
                    return new RegionResult(p.getId().toString(), p.getLargestRegion(), walls, minHeight);

                });
    }
}
