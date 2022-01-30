/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Repair extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;

    public Repair(Plugin plugin, Configuration configuration) {
        super(plugin, CommandMeta.builder("repair")
                .addUnlocalizedArgument("id", false)
                .withPermission(Permissions.Cluster.REPAIR)
                .build());
        this.configuration = configuration;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        Optional<GridCluster> cluster;
        if (args.isEmpty()) {
            cluster = configuration.cluster().world(player.getWorld()).getCluster(player.getLocation());
            CommandAssertions.isTrue(cluster.isPresent(), "You are not inside a cluster");
        } else {
            cluster = configuration.cluster().world(player.getWorld()).getCluster(args.asInt(0));
            CommandAssertions.isTrue(cluster.isPresent(), "Unkown cluster id");
        }

        Draw.draw(cluster.get(), player);
        configuration.save();
        messageSender().sendMessage(player, "Cluster redrawn.");
    }
}
