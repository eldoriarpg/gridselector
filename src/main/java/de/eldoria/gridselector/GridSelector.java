package de.eldoria.gridselector;


import com.plotsquared.core.PlotSquared;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.gridselector.adapter.WorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.GridWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.PlotWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.command.Grid;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.GridWorld;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.selector.GridProvider;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GridSelector extends EldoPlugin {

    @Override
    public void onPluginEnable() throws Throwable {
        var config = new Configuration(this);

        var sbr = (SchematicBrushReborn) getPluginManager().getPlugin("SchematicBrushReborn");

        var gridSchematics = new GridSchematics(this);

        sbr.brushSettingsRegistry().registerSelector(new GridProvider(sbr.schematics()));
        sbr.schematics().register(GridSchematics.KEY, gridSchematics);

        List<RegionAdapter> regionAdapters = new ArrayList<>();
        regionAdapters.add(new GridWorldAdapter(config));

        if (getPluginManager().isPluginEnabled("PlotSquared")) {
            logger().info("Found plot squared. Registering plot squared world adapter");
            var plotSquared = PlotSquared.get();
            regionAdapters.add(new PlotWorldAdapter(plotSquared));
        }

        var worldAdapter = new WorldAdapter(regionAdapters);
        var selectionListener = new SelectionListener(worldAdapter, gridSchematics);

        registerListener(selectionListener);

        registerCommand(new Grid(this, selectionListener, config));
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(GridWorld.class);
    }
}
