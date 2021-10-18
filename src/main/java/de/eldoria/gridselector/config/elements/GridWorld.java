package de.eldoria.gridselector.config.elements;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.eldoria.eldoutilities.container.Pair;
import de.eldoria.eldoutilities.serialization.SerializationUtil;
import de.eldoria.gridselector.util.Grid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GridWorld implements ConfigurationSerializable {
    private String name;
    private Vector centerOffset;
    private int gridSize;

    public GridWorld(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        name = map.getValue("name");
        centerOffset = map.getValue("center");
        gridSize = map.getValue("gridSize");
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("name", name)
                .add("center", centerOffset)
                .add("gridSize", gridSize)
                .build();
    }

    public String name() {
        return name;
    }

    public Vector centerOffset() {
        return centerOffset;
    }

    public int gridSize() {
        return gridSize;
    }

    public String getGridItentifier(Location loc){
        var coord = Grid.calcGridCoord(gridSize, loc.getX(), loc.getZ());
        return String.format("%s;%s", coord.first, coord.second);
    }

    public CuboidRegion getGridElement(Location loc) {
        var gridCoord = Grid.calcGridCoord(gridSize, loc.getX(), loc.getZ());
        var boundaries = Grid.calcGridBoundaries(gridSize, gridCoord);
        var world = loc.getWorld();
        var min = boundaries.first.setY(world.getMinHeight());
        var max = boundaries.second.setY(world.getMaxHeight());

        return new CuboidRegion(BukkitAdapter.adapt(world),
                BukkitAdapter.adapt(min.toLocation(world)).toVector().toBlockPoint(),
                BukkitAdapter.adapt(max.toLocation(world)).toVector().toBlockPoint());
    }
}
