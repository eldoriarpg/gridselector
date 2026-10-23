/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.util.Colors;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Lists the clusters of the player's world with their IDs.
 */
public class ListClusters extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;

    public ListClusters(Plugin plugin, Configuration configuration) {
        super(
                plugin,
                CommandMeta.builder("list")
                        .withPermission(
                                Permissions.Cluster.CREATE, Permissions.Cluster.REMOVE, Permissions.Cluster.REPAIR)
                        .build());
        this.configuration = configuration;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args)
            throws CommandException {
        var clusters = configuration.cluster().world(player.getWorld()).cluster();
        if (clusters.isEmpty()) {
            messageSender().sendMessage(player, "There are no clusters in this world.");
            return;
        }
        var composer = MessageComposer.create().text("<%s>Clusters in this world", Colors.HEADING);
        for (var cluster : clusters) {
            composer.newLine().text(describe(cluster));
        }
        messageSender().sendMessage(player, composer.build());
    }

    private static String describe(GridCluster cluster) {
        var min = cluster.plot().min();
        var max = cluster.plot().max();
        return "<%s>%d: <%s>%d|%d to %d|%d, %d × %d plots of size %d"
                .formatted(
                        Colors.NAME,
                        cluster.id(),
                        Colors.VALUE,
                        min.x(),
                        min.z(),
                        max.x(),
                        max.z(),
                        cluster.rows(),
                        cluster.columns(),
                        cluster.elementSize());
    }
}
