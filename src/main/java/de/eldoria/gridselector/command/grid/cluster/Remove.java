/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.GridCluster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Remove extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;
    private WorldGuard worldGuard = null;

    public Remove(Plugin plugin, Configuration configuration) {
        super(plugin, CommandMeta.builder("remove")
                .addUnlocalizedArgument("id", false)
                .build());
        this.configuration = configuration;
        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuard = WorldGuard.getInstance();
        }
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        Optional<GridCluster> cluster;
        if (args.isEmpty()) {
            cluster = configuration.getClusterWorld(player.getWorld()).getCluster(player.getLocation());
            CommandAssertions.isTrue(cluster.isPresent(), "You are not inside a cluster");
        } else {
            cluster = configuration.getClusterWorld(player.getWorld()).getCluster(args.asInt(0));
            CommandAssertions.isTrue(cluster.isPresent(), "Unkown cluster id");
        }

        if (configuration.generalSettings().isCreateWorldGuardRegions() && worldGuard != null) {
            var world = BukkitAdapter.adapt(player.getWorld());
            worldGuard.getPlatform().getRegionContainer().get(world).removeRegion(cluster.get().boundingBox().id());
        }

        configuration.getClusterWorld(player.getWorld()).unregister(cluster.get().id());
        messageSender().sendMessage(player, "Cluster removed");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return IPlayerTabExecutor.super.onTabComplete(player, alias, args);
    }
}
