/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.gridselector.config.elements.ClusterWorld;
import de.eldoria.gridselector.config.elements.GridWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Configuration extends EldoConfig {
    private static final String GRID_WORLDS = "gridworlds";
    private List<GridWorld> gridWorlds;
    private Map<UUID, ClusterWorld> clusterWorlds = new HashMap<>();

    public Configuration(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void saveConfigs() {
        setVersion(1, false);
        loadConfig(GRID_WORLDS, null, false).set("gridWorlds", gridWorlds);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void reloadConfigs() {
        gridWorlds = (List<GridWorld>) loadConfig(GRID_WORLDS, null, true).getList("gridWorlds", new ArrayList<>());
    }

    public Optional<GridWorld> getGridWorld(String name) {
        return gridWorlds.stream().filter(w -> w.name().equals(name)).findAny();
    }

    public Optional<GridWorld> getGridWorld(@NotNull Location location) {
        if (location.getWorld() == null) return Optional.empty();
        return getGridWorld(location.getWorld());
    }

    public Optional<GridWorld> getGridWorld(World world) {
        return getGridWorld(world.getName());
    }

    public GridWorld createGridWorld(World world) {
        var gridWorld = getGridWorld(world).orElse(new GridWorld(world.getName()));
        if (!gridWorlds.contains(gridWorld)) {
            gridWorlds.add(gridWorld);
        }
        return gridWorld;
    }

    public ClusterWorld getClusterWorld(World world){
        return clusterWorlds.computeIfAbsent(world.getUID(), k-> new ClusterWorld(world));
    }
}
