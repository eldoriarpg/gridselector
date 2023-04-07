/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import de.eldoria.gridselector.config.elements.cluster.ClusterWorld;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
@SerializableAs("gridSelectorClusterWorlds")
public class ClusterWorlds implements ConfigurationSerializable {
    private Map<UUID, ClusterWorld> clusterWorlds = new HashMap<>();

    public ClusterWorlds() {
    }

    public ClusterWorlds(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        List<ClusterWorld> worlds = map.getValue("worlds");
        for (var world : worlds) {
            clusterWorlds.put(world.uid(), world);
        }
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("worlds", new ArrayList<>(clusterWorlds.values()))
                .build();
    }

    public ClusterWorld world(World world) {
        return clusterWorlds.computeIfAbsent(world.getUID(), k -> new ClusterWorld(world));
    }

    public Map<UUID, ClusterWorld> clusterWorlds() {
        return clusterWorlds;
    }

    public void clusterWorlds(Map<UUID, ClusterWorld> clusterWorlds) {
        this.clusterWorlds = clusterWorlds;
    }
}
