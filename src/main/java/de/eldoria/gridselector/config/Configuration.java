/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.gridselector.config.elements.ClusterWorld;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.GeneralSettings;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Configuration extends EldoConfig {
    private static final String CLUSTER_WORLDS = "clusterWorlds";
    private ClusterWorlds clusterWorlds = new ClusterWorlds();
    private GeneralSettings generalSettings = new GeneralSettings();

    public Configuration(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void saveConfigs() {
        setVersion(1, false);
        getConfig().set("generalSettings", generalSettings);
        loadConfig(CLUSTER_WORLDS, null, false).set("clusterWorlds", clusterWorlds);
    }

    @Override
    protected void reloadConfigs() {
        clusterWorlds = loadConfig(CLUSTER_WORLDS, null, true).getObject("clusterWorlds", ClusterWorlds.class);
        generalSettings = getConfig().getObject("generalSettings", GeneralSettings.class, new GeneralSettings());
    }

    public GeneralSettings generalSettings() {
        return generalSettings;
    }

    public ClusterWorld getClusterWorld(World world) {
        return clusterWorlds.getClusterWorld(world);
    }
}
