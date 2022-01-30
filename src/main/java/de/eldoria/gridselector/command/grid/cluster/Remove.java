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
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Remove extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;
    private final IWorldGuardAdapter worldGuardAdapter;

    public Remove(Plugin plugin, Configuration configuration, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("remove")
                .addUnlocalizedArgument("id", false)
                .withPermission(Permissions.Cluster.REMOVE)
                .build());
        this.configuration = configuration;
        this.worldGuardAdapter = worldGuardAdapter;
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

        worldGuardAdapter.unregister(cluster.get(), player);

        configuration.cluster().world(player.getWorld()).unregister(cluster.get().id());
        messageSender().sendMessage(player, "Cluster removed");
        configuration.save();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return IPlayerTabExecutor.super.onTabComplete(player, alias, args);
    }
}
