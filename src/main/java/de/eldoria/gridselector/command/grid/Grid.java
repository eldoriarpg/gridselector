/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.command.grid.grid.Define;
import de.eldoria.gridselector.command.grid.grid.Draw;
import de.eldoria.gridselector.config.Configuration;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin, Configuration configuration, RegionAdapter plotAdapter) {
        super(plugin, CommandMeta.builder("grid")
                .withSubCommand(new Define(plugin, configuration, plotAdapter))
                .withSubCommand(new Draw(plugin, configuration, plotAdapter))
                .build());
    }
}
