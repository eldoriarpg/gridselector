package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public record RegionResult(String identifier, CuboidRegion region) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionResult)) return false;

        var that = (RegionResult) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    public List<Vector> getCorners() {
        List<Vector> result = new ArrayList<>();
        var max = region.getMaximumPoint();
        var min = region.getMinimumPoint();
        for (var x : new int[]{min.getX() - 1, max.getX() + 1}) {
            for (var y : new int[]{min.getY() - 1, max.getY() + 1}) {
                for (var z : new int[]{min.getZ() - 1, max.getZ() + 1}) {
                    result.add(new Vector(x, y, z));
                }
            }
        }
        return result;
    }
}
