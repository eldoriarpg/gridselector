/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster.modify;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.command.grid.cluster.Sessions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Direction extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;

    public Direction(Plugin plugin, Sessions sessions) {
        super(plugin, CommandMeta.builder("direction")
                .build());
        this.sessions = sessions;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var direction = com.sk89q.worldedit.util.Direction.findClosest(BukkitAdapter.adapt(player).getLocation().getDirection(), com.sk89q.worldedit.util.Direction.Flag.CARDINAL);
        CommandAssertions.isTrue(direction != null, "Invalid direction. Please look in the requested direction");
        sessions.getOrCreateSession(player).direction(direction);
        sessions.showBuilder(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return IPlayerTabExecutor.super.onTabComplete(player, alias, args);
    }
}
