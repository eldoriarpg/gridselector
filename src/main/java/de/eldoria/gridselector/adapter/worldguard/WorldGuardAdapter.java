/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardAdapter implements IWorldGuardAdapter {
    private final WorldGuard worldGuard;
    private final Configuration configuration;

    public WorldGuardAdapter(Configuration configuration) {
        this.worldGuard = WorldGuard.getInstance();
        this.configuration = configuration;
    }

    @Override
    public void register(GridCluster cluster, World world, Player owner) {
        if (configuration.general().isCreateWorldGuardRegions() && worldGuard != null) {
            var weWorld = BukkitAdapter.adapt(world);
            var region = new ProtectedCuboidRegion(
                    cluster.plot().id(),
                    cluster.plot().min().toBlockVector3(weWorld.getMinY()),
                    cluster.plot().max().toBlockVector3(weWorld.getMaxY()));
            region.getOwners().addPlayer(owner.getUniqueId());
            worldGuard.getPlatform().getRegionContainer().get(weWorld).addRegion(region);
        }
    }

    /**
     * Removes the region whether or not regions are switched on, so a cluster created while they were on does not
     * leave its region behind.
     */
    @Override
    public void unregister(GridCluster cluster, World world) {
        if (worldGuard == null) return;
        var regions = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regions != null) regions.removeRegion(cluster.plot().id());
    }
}
