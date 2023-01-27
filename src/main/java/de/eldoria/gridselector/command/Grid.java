/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.GridSelector;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.command.grid.Cluster;
import de.eldoria.gridselector.command.grid.Export;
import de.eldoria.gridselector.command.grid.Select;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.messageblocker.blocker.MessageBlocker;

public class Grid extends AdvancedCommand {
    public Grid(GridSelector plugin, SelectionListener selectionListener, Configuration config,
                GridSchematics gridSchematics, MessageBlocker messageBlocker, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .buildSubCommands((advancedCommands, commandMetaBuilder) -> {
                    var cluster = new Cluster(plugin, messageBlocker, config, worldGuardAdapter);
                    commandMetaBuilder.withDefaultCommand(cluster);
                    advancedCommands.add(cluster);
                    advancedCommands.add(new Select(plugin, selectionListener));
                    advancedCommands.add(new Export(plugin, gridSchematics));
                })
                .build());
    }
}
