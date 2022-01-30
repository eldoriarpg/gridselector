/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.gridselector.GridSelector;
import de.eldoria.gridselector.adapter.worldguard.IWorldGuardAdapter;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Draw extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;
    private final Configuration configuration;
    private final IWorldGuardAdapter worldGuardAdapter;

    public Draw(Plugin plugin, Sessions sessions, Configuration configuration, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("draw")
                .withPermission(Permissions.Cluster.CREATE)
                .hidden()
                .build());
        this.sessions = sessions;
        this.configuration = configuration;
        this.worldGuardAdapter = worldGuardAdapter;
    }

    public static void draw(GridCluster cluster, Player player) throws CommandException {
        var min = cluster.plot().min();
        var max = cluster.plot().max();

        var y = cluster.minHeight();

        var actor = BukkitAdapter.adapt(player);

        var session = WorldEdit.getInstance().newEditSessionBuilder().actor(actor).world(BukkitAdapter.adapt(player.getWorld())).build();
        try (session) {
            for (var x = min.getBlockX(); x < max.getBlockX(); x++) {
                for (var z = min.getBlockZ(); z < max.getBlockZ(); z++) {
                    var loc = BlockVector3.at(x, y, z);
                    session.setBlock(loc, cluster.getBlock(loc.toBlockVector2()));
                }
            }
        } catch (MaxChangedBlocksException e) {
            throw CommandException.message("Grid exceeds maximum world edit size.");
        } finally {
            WorldEdit.getInstance().getSessionManager().get(actor).remember(session);
        }

        MessageSender.getPluginMessageSender(GridSelector.class).sendMessage(player, "Created grid. Changed " + session.size() + " blocks.");
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var builder = sessions.getOrCreateSession(player);

        var cluster = builder.build();

        var y = player.getWorld().getHighestBlockYAt(cluster.plot().min().getX(), cluster.plot().min().getZ());
        cluster.updateMinHeight(y);


        configuration.cluster().world(player.getWorld()).assertOverlap(cluster);
        draw(cluster, player);
        configuration.cluster().world(player.getWorld()).register(cluster);
        worldGuardAdapter.register(cluster, player);
        configuration.save();
        sessions.close(player);
    }
}
