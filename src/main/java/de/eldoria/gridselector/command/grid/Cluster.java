/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.command.grid.cluster.Create;
import de.eldoria.gridselector.command.grid.cluster.Draw;
import de.eldoria.gridselector.command.grid.cluster.Modify;
import de.eldoria.gridselector.command.grid.cluster.Remove;
import de.eldoria.gridselector.command.grid.cluster.Repair;
import de.eldoria.gridselector.command.grid.cluster.Sessions;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.messageblocker.blocker.IMessageBlockerService;
import org.bukkit.plugin.Plugin;

public class Cluster extends AdvancedCommand {
    public Cluster(Plugin plugin, IMessageBlockerService messageBlocker, Configuration configuration) {
        super(plugin, CommandMeta.builder("cluster")
                .buildSubCommands((cmds, builder) -> {
                    Sessions sessions = new Sessions(plugin, messageBlocker);
                    var create = new Create(plugin, sessions);
                    cmds.add(create);
                    cmds.add(new Draw(plugin, sessions, configuration));
                    cmds.add(new Modify(plugin, sessions));
                    cmds.add(new Repair(plugin, configuration));
                    cmds.add(new Remove(plugin,configuration));
                    cmds.add(new Modify(plugin, sessions));
                    builder.withDefaultCommand(create);
                })
                .build());
    }
}
