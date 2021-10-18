package de.eldoria.gridselector;


import com.plotsquared.core.PlotSquared;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.gridselector.adapter.WorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.GridWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.PlotWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.command.Grid;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.listener.SelectionListener;

import java.util.ArrayList;
import java.util.List;

public class GridSelector extends EldoPlugin {

    @Override
    public void onPluginEnable() throws Throwable {
        var config = new Configuration(this);

        List<RegionAdapter> regionAdapters = new ArrayList<>();
        regionAdapters.add(new GridWorldAdapter(config));

        if (getPluginManager().isPluginEnabled("PlotSquared")) {
            logger().info("Found plot squared. Registering plot squared world adapter");
            var plotSquared = PlotSquared.get();
            regionAdapters.add(new PlotWorldAdapter(plotSquared));
        }

        var worldAdapter = new WorldAdapter(regionAdapters);
        var selectionListener = new SelectionListener(this, worldAdapter);

        registerListener(selectionListener);

        registerCommand(new Grid(this, selectionListener));
    }
}
