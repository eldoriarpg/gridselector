/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements.cluster;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SerializableAs("gridSelectorClusterWorld")
public class ClusterWorld implements ConfigurationSerializable {
    private final UUID uid;
    private final List<GridCluster> cluster;
    private int id = 1;

    public ClusterWorld(World world) {
        this.uid = world.getUID();
        cluster = new ArrayList<>();
    }

    public ClusterWorld(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        uid = UUID.fromString(map.getValue("UID"));
        cluster = map.getValue("cluster");
        id = map.getValue("id");
    }

    /**
     * Registers a new cluster for the cluster world
     *
     * @param cluster cluster to register
     * @throws CommandException when the cluster overlaps with another cluster
     */
    public void register(GridCluster cluster) throws CommandException {
        assertOverlap(cluster);
        cluster.id(id++);
        this.cluster.add(cluster);
    }

    /**
     * Checks if the cluster overlaps with any other cluster in this cluster world
     *
     * @param cluster cluster to check
     * @throws CommandException when the cluster overlaps with another cluster
     */
    public void assertOverlap(GridCluster cluster) throws CommandException {
        for (var gridCluster : this.cluster) {
            CommandAssertions.isFalse(gridCluster.plot().overlaps(cluster.plot()), "Cluster overlaps with cluster " + gridCluster.id() + ". Change dimensions/position or remove other cluster first.");
        }
    }

    /**
     * Unregisters a cluster from the cluster world
     *
     * @param id id to unregister
     * @return true if a cluster was removed.
     */
    public boolean unregister(int id) {
        return cluster.removeIf(c -> c.id() == id);
    }

    /**
     * Unregisters a cluster from the cluster world
     *
     * @param cluster cluster to unregister
     * @return true if a cluster was removed.
     */
    public boolean unregister(GridCluster cluster) {
        return unregister(cluster.id());
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("UID", uid.toString())
                .add("id", id)
                .add("cluster", cluster)
                .build();
    }

    /**
     * Gets the cluster at the location if a cluster is present. This does not mean that the cluster will also return a region when
     * calling {@link GridCluster#getRegion(com.sk89q.worldedit.util.Location)}
     *
     * @param location location
     * @return grid cluster if present
     */
    public Optional<GridCluster> getCluster(Location location) {
        var vec2 = BukkitAdapter.adapt(location).toVector().toBlockPoint().toBlockVector2();
        return cluster.stream().filter(c -> c.contains(vec2)).findFirst();
    }

    /**
     * Gets a cluster by id
     *
     * @param id cluster id
     * @return cluster if existent
     */
    public Optional<GridCluster> getCluster(int id) {
        return cluster.stream().filter(c -> c.id() == id).findFirst();
    }

    /**
     * Immutable list of all clusters
     *
     * @return immutable cluster list
     */
    public List<GridCluster> cluster() {
        return Collections.unmodifiableList(cluster);
    }

    /**
     * UID of world
     *
     * @return UID of world
     */
    public UUID uid() {
        return uid;
    }
}
