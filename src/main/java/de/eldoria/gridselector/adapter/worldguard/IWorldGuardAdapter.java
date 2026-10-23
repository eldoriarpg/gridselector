/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.worldguard;

import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface IWorldGuardAdapter {
    IWorldGuardAdapter DEFAULT = new IWorldGuardAdapter() {
        @Override
        public void register(GridCluster cluster, World world, Player owner) {

        }

        @Override
        public void unregister(GridCluster cluster, World world) {

        }
    };

    /**
     * Protects the cluster with a region, if regions are switched on.
     *
     * @param cluster the cluster to protect
     * @param world   the world of the cluster
     * @param owner   the owner of the region
     */
    void register(GridCluster cluster, World world, Player owner);

    /**
     * Removes the region of the cluster, if it has one.
     *
     * @param cluster the cluster whose region is removed
     * @param world   the world of the cluster
     */
    void unregister(GridCluster cluster, World world);
}
