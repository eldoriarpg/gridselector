/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
public class ClusterWorlds {
    private Map<UUID, ClusterWorld> clusterWorlds = new HashMap<>();

    public ClusterWorlds() {
    }

    public ClusterWorld world(World world) {
        return clusterWorlds.computeIfAbsent(world.getUID(), k -> new ClusterWorld(world));
    }

    public Map<UUID, ClusterWorld> clusterWorlds() {
        return clusterWorlds;
    }
}
