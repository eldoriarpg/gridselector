/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.config.ConfigKey;
import de.eldoria.eldoutilities.config.JacksonConfig;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class JacksonConfiguration extends JacksonConfig<ConfigFile> implements Configuration {
    public static final ConfigKey<ClusterWorlds> CLUSTER_WORLDS =
            ConfigKey.of("Cluster Worlds", Path.of("clusterWorlds.yml"), ClusterWorlds.class, ClusterWorlds::new);

    public JacksonConfiguration(@NotNull Plugin plugin) {
        super(plugin, ConfigKey.defaultConfig(ConfigFile.class, ConfigFile::new));
    }

    @Override
    public General general() {
        return main().general();
    }

    @Override
    public Highlight highlight() {
        return main().highlight();
    }

    @Override
    public ClusterWorlds cluster() {
        return secondary(CLUSTER_WORLDS);
    }

    @Override
    public ClusterWorld getClusterWorld(World world) {
        return cluster().world(world);
    }
}
