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
import com.sk89q.worldedit.util.Location;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.GridCluster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Draw extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;
    private final Configuration configuration;

    public Draw(Plugin plugin, Sessions sessions, Configuration configuration) {
        super(plugin, CommandMeta.builder("draw")
                .hidden()
                .build());
        this.sessions = sessions;
        this.configuration = configuration;
    }


    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var builder = sessions.getOrCreateSession(player);
        sessions.close(player);

        var cluster = builder.build();

        configuration.getClusterWorld(player.getWorld()).register(cluster);
        draw(cluster, player);
    }

    public static void draw(GridCluster cluster, Player player) {
        var min = cluster.boundingBox().getMin();
        var max = cluster.boundingBox().getMax();

        var y = player.getWorld().getHighestBlockYAt(min.toLocation(player.getWorld()));

        var actor = BukkitAdapter.adapt(player);

        var localSession = WorldEdit.getInstance().getSessionManager().get(actor);
        var session = WorldEdit.getInstance().newEditSessionBuilder().actor(actor).world(BukkitAdapter.adapt(player.getWorld())).build();
        try (session) {
            for (var x = min.getBlockX(); x < max.getBlockX(); x++) {
                for (var z = min.getBlockZ(); z < max.getBlockZ(); z++) {
                    var loc = BlockVector3.at(x, y, z);
                    session.setBlock(loc, cluster.getBlock(new Location(BukkitAdapter.adapt(player.getWorld()), loc.toVector3())));
                }
            }
        } catch (MaxChangedBlocksException e) {

        } finally {
            localSession.remember(session);
        }

        WorldEdit.getInstance().getSessionManager().get(actor).remember(session);
    }
}
