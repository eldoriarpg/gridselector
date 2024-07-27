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
import de.eldoria.gridselector.config.LegacyConfiguration;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.config.elements.cluster.Plot;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.selector.GridProvider;
import de.eldoria.messageblocker.MessageBlockerAPI;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GridSelector extends EldoPlugin {

    private JacksonConfiguration configuration;

    public GridSelector() {
        this.configuration = new JacksonConfiguration(this);
    }

    public GridSelector(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        this.configuration = new JacksonConfiguration(this);
    }

    @Override
    public void onPluginEnable() throws Throwable {

        configuration = new JacksonConfiguration(this);
        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            var legacyConfiguration = new LegacyConfiguration(this);
            getLogger().log(Level.INFO, "Migrating configuration to jackson.");
            configuration.main().general(legacyConfiguration.general());
            configuration.main().highlight(legacyConfiguration.highlight());
            configuration.replace(JacksonConfiguration.CLUSTER_WORLDS, legacyConfiguration.cluster());
            base.version(1);
            base.lastInstalledVersion(this);
            configuration.save();
        }

        var sbr = SchematicBrushReborn.instance();

        var iLocalizer = Localizer.builder(this, "en_US")
                .setIncludedLocales("en_US")
                .build();
        ILocalizer.getPluginLocalizer(SchematicBrushReborn.class).registerChild(iLocalizer);
        var messageSender = MessageSender.builder(this)
                .prefix("<gold>[GS]")
                .localizer(iLocalizer)
                .register();

        var messageBlocker = MessageBlockerAPI.builder(this).addWhitelisted("[GS]").build();

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

        registerCommand(new Grid(this, selectionListener, configuration, gridSchematics, messageBlocker, worldGuardAdapter));
    }

    public Configuration configuration() {
        return configuration;
    }

    @Override
    public Level getLogLevel() {
        return configuration.secondary(PluginBaseConfiguration.KEY).logLevel();
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(GridCluster.class, Plot.class, ClusterWorld.class, ClusterWorlds.class, Highlight.class, General.class,
                de.eldoria.gridselector.selector.Grid.class);
    }
}
