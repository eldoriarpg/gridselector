/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.gridselector.config.elements.ClusterWorlds;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import org.bukkit.World;

public interface Configuration {
    void save();

    General general();

    Highlight highlight();

    ClusterWorlds cluster();

    @Deprecated
    ClusterWorld getClusterWorld(World world);
}
