/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.config.JacksonConfig;
import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import dev.chojo.ocular.key.Key;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class JacksonConfiguration extends JacksonConfig<ConfigFile> implements Configuration {
    public static final Key<ClusterWorlds> CLUSTER_WORLDS =
            Key.builder(Path.of("clusterWorlds.yml"), ClusterWorlds::new).name("Cluster Worlds").build();

    public JacksonConfiguration(@NotNull Plugin plugin) {
        super(plugin, Key.builder(Path.of("config.yml"), ConfigFile::new).build());
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
