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
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Create extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;

    public Create(Plugin plugin, Sessions sessions) {
        super(plugin, CommandMeta.builder("create")
                .withPermission(Permissions.Cluster.CREATE)
                .build());
        this.sessions = sessions;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        sessions.getOrCreateSession(player);
        sessions.showBuilder(player);
    }
}
