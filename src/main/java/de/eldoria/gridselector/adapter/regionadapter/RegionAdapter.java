package de.eldoria.gridselector.adapter.regionadapter;

import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Location;

import java.util.Optional;

public interface RegionAdapter {
    /**
     * Checks if a adapter is applicable to the world of the location
     *
     * @param location location to determine the world
     * @return true of the adapter is applicable to the world
     */
    boolean isApplicable(Location location);

    /**
     * Get the region at a location.
     *
     * @param location location
     * @return the region at the location of there is one
     */
    Optional<RegionResult> getRegion(Location location);
}
