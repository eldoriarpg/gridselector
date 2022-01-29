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
import org.bukkit.entity.Player;

public class WorldGuardAdapter implements IWorldGuardAdapter {
    private final WorldGuard worldGuard;
    private final Configuration configuration;

    public WorldGuardAdapter(Configuration configuration) {
        this.worldGuard = WorldGuard.getInstance();
        this.configuration = configuration;
    }

    @Override
    public void register(GridCluster cluster, Player player) {
        if (configuration.generalSettings().isCreateWorldGuardRegions() && worldGuard != null) {
            var world = BukkitAdapter.adapt(player.getWorld());
            var region = new ProtectedCuboidRegion(cluster.boundingBox().id(),
                    cluster.boundingBox().min().toBlockVector3(world.getMinY()),
                    cluster.boundingBox().max().toBlockVector3(world.getMaxY()));
            region.getOwners().addPlayer(player.getUniqueId());
            worldGuard.getPlatform().getRegionContainer().get(world).addRegion(region);
        }
    }

    @Override
    public void unregister(GridCluster cluster, Player player){
        if (configuration.generalSettings().isCreateWorldGuardRegions() && worldGuard != null) {
            var world = BukkitAdapter.adapt(player.getWorld());
            worldGuard.getPlatform().getRegionContainer().get(world).removeRegion(cluster.boundingBox().id());
        }
    }
}
