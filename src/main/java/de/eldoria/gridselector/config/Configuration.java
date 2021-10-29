package de.eldoria.gridselector.config;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.gridselector.config.elements.GridWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Configuration extends EldoConfig {

    private List<GridWorld> gridWorlds;

    public Configuration(Plugin plugin) {
        super(plugin);
    }


    @Override
    protected void saveConfigs() {
        getConfig().set("gridWorlds", gridWorlds);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void reloadConfigs() {
        gridWorlds = (List<GridWorld>) getConfig().getList("gridWorlds", new ArrayList<>());
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
}
