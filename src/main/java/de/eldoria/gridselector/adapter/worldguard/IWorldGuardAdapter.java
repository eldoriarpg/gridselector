/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.worldguard;

import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import org.bukkit.entity.Player;

public interface IWorldGuardAdapter {
    public static IWorldGuardAdapter DEFAULT = new IWorldGuardAdapter() {
        @Override
        public void register(GridCluster cluster, Player player) {

        }

        @Override
        public void unregister(GridCluster cluster, Player player) {

        }
    };

    void register(GridCluster cluster, Player player);

    void unregister(GridCluster cluster, Player player) ;
}
