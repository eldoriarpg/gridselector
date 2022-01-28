/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ClusterWorld implements ConfigurationSerializable {
    private final UUID name;
    private final List<GridCluster> cluster;
    private int id = 1;

    public ClusterWorld(World world) {
        this.name = world.getUID();
        cluster = new ArrayList<>();
    }

    public ClusterWorld(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        name = UUID.fromString(map.getValue("name"));
        cluster = map.getValue("cluster");
        id = map.getValue("id");
    }

    public void register(GridCluster cluster) throws CommandException {
        for (var gridCluster : this.cluster) {
            CommandAssertions.isFalse(gridCluster.boundingBox().overlaps(cluster.boundingBox()), "Cluster overlaps with cluster " + gridCluster.id() + ". Change dimensions/position or remove other cluster first.");
        }
        cluster.id(id++);
        this.cluster.add(cluster);
    }

    public boolean unregister(int id) {
        return cluster.removeIf(c -> c.id() == id);
    }


    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("name", name.toString())
                .add("id", id)
                .add("cluster", cluster)
                .build();
    }

    public Optional<GridCluster> getCluster(Location location) {
        return cluster.stream().filter(c -> c.contains(location)).findFirst();
    }
}
