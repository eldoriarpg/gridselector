/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.command.grid.cluster.Close;
import de.eldoria.gridselector.command.grid.cluster.Create;
import de.eldoria.gridselector.command.grid.cluster.Draw;
import de.eldoria.gridselector.command.grid.cluster.Modify;
import de.eldoria.gridselector.command.grid.cluster.Remove;
import de.eldoria.gridselector.command.grid.cluster.Repair;
import de.eldoria.gridselector.command.grid.cluster.Sessions;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.util.Permissions;
import de.eldoria.messageblocker.blocker.MessageBlocker;
import org.bukkit.plugin.Plugin;

public class Cluster extends AdvancedCommand {
    public Cluster(Plugin plugin, MessageBlocker messageBlocker, Configuration configuration, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("cluster")
                .withPermission(Permissions.Cluster.REMOVE, Permissions.Cluster.CREATE, Permissions.Cluster.REPAIR)
                .buildSubCommands((cmds, builder) -> {
                    var sessions = new Sessions(plugin, messageBlocker);
                    var create = new Create(plugin, sessions);
                    var draw = new Draw(plugin, sessions, configuration, worldGuardAdapter);
                    cmds.add(create);
                    cmds.add(draw);
                    cmds.add(new Modify(plugin, sessions));
                    cmds.add(new Repair(plugin, configuration, draw));
                    cmds.add(new Remove(plugin, configuration, worldGuardAdapter));
                    cmds.add(new Modify(plugin, sessions));
                    cmds.add(new Close(plugin, sessions));
                    builder.withDefaultCommand(create);
                })
                .build());
    }
}
