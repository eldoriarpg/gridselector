/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
    private final WorldEdit worldEdit;

    public Draw(Plugin plugin, Sessions sessions, Configuration configuration, IWorldGuardAdapter worldGuardAdapter) {
        super(plugin, CommandMeta.builder("draw")
                .withPermission(Permissions.Cluster.CREATE)
                .hidden()
                .build());
        this.sessions = sessions;
        this.configuration = configuration;
        this.worldGuardAdapter = worldGuardAdapter;
        worldEdit = WorldEdit.getInstance();
    }

    public void draw(GridCluster cluster, Player player) {
        var y = cluster.minHeight();

        var actor = BukkitAdapter.adapt(player);

        var session = worldEdit.newEditSessionBuilder().actor(actor).world(BukkitAdapter.adapt(player.getWorld())).build();
        try (session) {
            drawRegions(cluster, session, y, player);
        } catch (MaxChangedBlocksException e) {
            messageSender().sendError(player, "Grid exceeds maximum world edit size.");
        }
        worldEdit.getSessionManager().get(actor).remember(session);
        messageSender().sendMessage(player, "Created grid. Changed " + session.size() + " blocks.");
    }

    private void drawRegions(GridCluster cluster, EditSession session, int y, Player player) throws MaxChangedBlocksException {
        session.setBlocks(cluster.plot().as2DRegion(y), BukkitAdapter.adapt(cluster.offsetMaterial().createBlockData()));
        for (var plot : cluster.getRegions()) {
            session.setBlocks(plot.as2DRegion(y), BukkitAdapter.adapt(cluster.borderMaterial().createBlockData()));
            session.setBlocks(plot.borderLessPlot().as2DRegion(y), BukkitAdapter.adapt(cluster.floorMaterial().createBlockData()));
        }
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var builder = sessions.getOrCreateSession(player);

        var cluster = builder.build();

        var y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        cluster.updateMinHeight(y);


        configuration.cluster().world(player.getWorld()).assertOverlap(cluster);
        draw(cluster, player);
        configuration.cluster().world(player.getWorld()).register(cluster);
        worldGuardAdapter.register(cluster, player);
        configuration.save();
        sessions.close(player);
    }
}
