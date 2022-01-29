/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector;


import com.plotsquared.core.PlotSquared;
import de.eldoria.eldoutilities.localization.ILocalizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.gridselector.adapter.regionadapter.ClusterWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.PlotWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionResult;
import de.eldoria.gridselector.adapter.regionadapter.WorldAdapter;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.adapter.worldguard.WorldGuardAdapter;
import de.eldoria.gridselector.command.Grid;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.GeneralSettings;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.config.elements.cluster.Plot;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.selector.GridProvider;
import de.eldoria.messageblocker.MessageBlockerAPI;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GridSelector extends EldoPlugin {

    @Override
    public void onPluginEnable() throws Throwable {
        var configuration = new Configuration(this);

        var sbr = (SchematicBrushReborn) Objects.requireNonNull(getPluginManager().getPlugin("SchematicBrushReborn"));

        var messageSender = MessageSender.create(this, "§3[GS]");
        var messageBlocker = MessageBlockerAPI.builder(this).addWhitelisted("[GS]").build();

        var gridSchematics = new GridSchematics(this);

        var iLocalizer = ILocalizer.create(this, "en_US");
        iLocalizer.setLocale("en_US");
        sbr.brushSettingsRegistry().registerSelector(new GridProvider(sbr.schematics()));
        sbr.schematics().register(GridSchematics.KEY, gridSchematics);

        List<RegionAdapter> regionAdapters = new ArrayList<>();
        regionAdapters.add(new ClusterWorldAdapter(configuration));

        RegionAdapter plotWorldAdapter;
        if (getPluginManager().isPluginEnabled("PlotSquared")) {
            logger().info("Found plot squared. Registering plot squared world adapter");
            var plotSquared = PlotSquared.get();
            plotWorldAdapter = new PlotWorldAdapter(plotSquared);
            regionAdapters.add(plotWorldAdapter);
        } else {
            plotWorldAdapter = new RegionAdapter() {
                @Override
                public boolean isApplicable(Location location) {
                    return false;
                }

                @Override
                public Optional<RegionResult> getRegion(Location location) {
                    return Optional.empty();
                }
            };
        }

        var worldAdapter = new WorldAdapter(regionAdapters);
        var selectionListener = new SelectionListener(worldAdapter, gridSchematics, messageSender, configuration);

        registerListener(selectionListener);

        var worldGuardAdapter = IWorldGuardAdapter.DEFAULT;

        if (getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardAdapter = new WorldGuardAdapter(configuration);
        }

        registerCommand(new Grid(this, sbr, selectionListener, configuration, gridSchematics, messageBlocker, worldGuardAdapter));
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(GridCluster.class, Plot.class, ClusterWorld.class, ClusterWorlds.class, GeneralSettings.class);
    }
}
