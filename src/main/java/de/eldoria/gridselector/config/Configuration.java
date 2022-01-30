/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Configuration extends EldoConfig {
    private static final String CLUSTER_WORLDS = "clusterWorlds";
    private ClusterWorlds cluster;
    private General general;
    private Highlight highlight;

    public Configuration(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void saveConfigs() {
        setVersion(1, false);

        //Config
        getConfig().set("general", general);
        getConfig().set("highlight", highlight);

        // External files
        loadConfig(CLUSTER_WORLDS, null, false).set("clusterWorlds", cluster);
    }

    @Override
    protected void reloadConfigs() {
        // Config
        general = getConfig().getObject("general", General.class, new General());
        highlight = getConfig().getObject("highlight", Highlight.class, new Highlight());

        // External files
        cluster = loadConfig(CLUSTER_WORLDS, null, true).getObject("clusterWorlds", ClusterWorlds.class, new ClusterWorlds());
    }

    public General general() {
        return general;
    }

    public Highlight highlight() {
        return highlight;
    }

    public ClusterWorlds cluster() {
        return cluster;
    }

    @Deprecated
    public ClusterWorld getClusterWorld(World world) {
        return cluster.world(world);
    }
}
