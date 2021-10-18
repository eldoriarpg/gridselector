package de.eldoria.gridselector.adapter;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorldAdapter {
    private final List<RegionAdapter> adapters;

    public WorldAdapter(List<RegionAdapter> adapters) {
        this.adapters = adapters;
    }

    public Optional<RegionResult> getRegion(Location location) {
        var loc = BukkitAdapter.adapt(location);
        return adapters.stream()
                .filter(a -> a.isApplicable(loc))
                .findFirst()
                .flatMap(adaper -> adaper.getRegion(loc));
    }
}
