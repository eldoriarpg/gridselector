/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster.modify;

import de.eldoria.eldoutilities.commands.Completion;
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

public class Rows extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;

    public Rows(Plugin plugin, Sessions sessions) {
        super(plugin, CommandMeta.builder("rows")
                .addUnlocalizedArgument("amount", true)
                .build());
        this.sessions = sessions;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        CommandAssertions.min(args.asInt(0), 1);
        sessions.getOrCreateSession(player).rows(args.asInt(0));
        sessions.showBuilder(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return Completion.completeMinInt(args.asString(0), 1);
    }
}
