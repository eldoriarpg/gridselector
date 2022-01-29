/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.command.grid.cluster.modify.BorderMaterial;
import de.eldoria.gridselector.command.grid.cluster.modify.Center;
import de.eldoria.gridselector.command.grid.cluster.modify.Columns;
import de.eldoria.gridselector.command.grid.cluster.modify.Direction;
import de.eldoria.gridselector.command.grid.cluster.modify.ExpandRight;
import de.eldoria.gridselector.command.grid.cluster.modify.FloorMaterial;
import de.eldoria.gridselector.command.grid.cluster.modify.Offset;
import de.eldoria.gridselector.command.grid.cluster.modify.OffsetMaterial;
import de.eldoria.gridselector.command.grid.cluster.modify.Rows;
import de.eldoria.gridselector.command.grid.cluster.modify.Size;
import org.bukkit.plugin.Plugin;

public class Modify extends AdvancedCommand {
    public Modify(Plugin plugin, Sessions sessions) {
        super(plugin, CommandMeta.builder("modify")
                .hidden()
                .withSubCommand(new Center(plugin, sessions))
                .withSubCommand(new Direction(plugin, sessions))
                .withSubCommand(new ExpandRight(plugin, sessions))
                .withSubCommand(new Size(plugin, sessions))
                .withSubCommand(new Rows(plugin, sessions))
                .withSubCommand(new Columns(plugin, sessions))
                .withSubCommand(new Offset(plugin, sessions))
                .withSubCommand(new FloorMaterial(plugin, sessions))
                .withSubCommand(new BorderMaterial(plugin, sessions))
                .withSubCommand(new OffsetMaterial(plugin, sessions))
                .withSubCommand(new Center(plugin, sessions))
                .build());
    }
}
