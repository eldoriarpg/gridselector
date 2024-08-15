/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Select extends AdvancedCommand implements IPlayerTabExecutor {
    private final SelectionListener selectionListener;

    public Select(Plugin plugin, SelectionListener selectionListener) {
        super(plugin, CommandMeta
                .builder("select")
                .withPermission(Permissions.USE)
                .build());
        this.selectionListener = selectionListener;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        selectionListener.register(player);
    }
}
