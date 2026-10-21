/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector;


import com.plotsquared.core.PlotSquared;
import de.eldoria.eldoutilities.config.template.PluginBaseConfiguration;
import de.eldoria.eldoutilities.localization.ILocalizer;
import de.eldoria.eldoutilities.localization.Localizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.eldoutilities.updater.lynaupdater.LynaUpdateChecker;
import de.eldoria.eldoutilities.updater.lynaupdater.LynaUpdateData;
import de.eldoria.gridselector.adapter.regionadapter.ClusterWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.PlotWorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.adapter.regionadapter.WorldAdapter;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.adapter.worldguard.WorldGuardAdapter;
import de.eldoria.gridselector.command.Grid;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.JacksonConfiguration;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.selector.GridProvider;
import de.eldoria.schematicbrush.SchematicBrushReborn;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GridSelector extends EldoPlugin {

    private JacksonConfiguration configuration;
    private Localizer iLocalizer;

    public GridSelector() {
        this.configuration = new JacksonConfiguration(this);
    }

    @Override
    public void onPluginEnable() throws Throwable {

        configuration = new JacksonConfiguration(this);
        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            if (isPreJacksonInstall()) {
                throw new IllegalStateException("The configuration was written by a version older than 1.3.0. Update to the latest 1.3 release first, start the server once, then update to this version.");
            }
            base.version(1);
            base.lastInstalledVersion(this);
            configuration.save();
        }

        var sbr = SchematicBrushReborn.instance();

        iLocalizer = Localizer.builder(this, "en_US")
                .setIncludedLocales("de_DE")
                .setUserLocale(p -> sbr.config().general().language())
                .build();
        var messageSender = MessageSender.builder(this)
                .prefix("<gold>[GS]")
                .localizer(iLocalizer)
                .register();

        var gridSchematics = new GridSchematics(this);

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
        }

        var worldAdapter = new WorldAdapter(regionAdapters);
        var selectionListener = new SelectionListener(worldAdapter, gridSchematics, messageSender, configuration);

        registerListener(selectionListener);

        var worldGuardAdapter = IWorldGuardAdapter.DEFAULT;

        if (getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardAdapter = new WorldGuardAdapter(configuration);
        }

        if (configuration.main().checkUpdates()) {
            LynaUpdateChecker.lyna(LynaUpdateData.builder(this, 8).build()).start();
        }

        registerCommand(new Grid(this, selectionListener, configuration, gridSchematics, worldGuardAdapter));
    }

    @Override
    public void onPostStart() throws Throwable {

        ILocalizer.getPluginLocalizer(SchematicBrushReborn.instance()).registerChild(iLocalizer);
    }

    /**
     * Checks whether the data folder still holds a configuration in the Bukkit serialization format used before the
     * switch to Jackson.
     *
     * @return true if the main configuration contains Bukkit serialization markers
     * @throws IOException if the configuration file can not be read
     */
    private boolean isPreJacksonInstall() throws IOException {
        var config = getDataFolder().toPath().resolve("config.yml");
        return Files.exists(config) && Files.readString(config).contains("==:");
    }

    public Configuration configuration() {
        return configuration;
    }

    @Override
    public Level getLogLevel() {
        return configuration.secondary(PluginBaseConfiguration.KEY).logLevel();
    }
}
