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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Repair extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;

    public Repair(Plugin plugin, Configuration configuration) {
        super(plugin, CommandMeta.builder("repair")
                .addUnlocalizedArgument("id", true)
                .build());
        this.configuration = configuration;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var cluster = configuration.getClusterWorld(player.getWorld()).getCluster(player.getLocation());
        CommandAssertions.isTrue(cluster.isPresent(), "Unkown cluster id");
        Draw.draw(cluster.get(), player);
        messageSender().sendMessage(player, "Cluster redrawn.");
    }
}
