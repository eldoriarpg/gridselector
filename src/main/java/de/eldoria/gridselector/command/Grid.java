/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.command.grid.Cluster;
import de.eldoria.gridselector.command.grid.SaveSchematics;
import de.eldoria.gridselector.command.grid.Select;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.messageblocker.blocker.IMessageBlockerService;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin, SchematicBrushReborn schematicBrushReborn, SelectionListener selectionListener, Configuration config,
                GridSchematics gridSchematics, IMessageBlockerService messageBlocker, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .withSubCommand(new Cluster(plugin, messageBlocker, config, worldGuardAdapter))
                .withSubCommand(new Select(plugin, selectionListener))
                .withSubCommand(new SaveSchematics(plugin, schematicBrushReborn, gridSchematics))
                .build());
    }
}
