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
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.gridselector.GridSelector;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.config.elements.GridCluster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Draw extends AdvancedCommand implements IPlayerTabExecutor {
    private final Sessions sessions;
    private final Configuration configuration;
    private WorldGuard worldGuard = null;

    public Draw(Plugin plugin, Sessions sessions, Configuration configuration) {
        super(plugin, CommandMeta.builder("draw")
                .hidden()
                .build());
        this.sessions = sessions;
        this.configuration = configuration;
        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuard = WorldGuard.getInstance();
        }
    }


    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var builder = sessions.getOrCreateSession(player);
        sessions.close(player);

        var cluster = builder.build();

        configuration.getClusterWorld(player.getWorld()).register(cluster);
        configuration.save();
        draw(cluster, player);
        if (configuration.generalSettings().isCreateWorldGuardRegions() && worldGuard != null) {
            var world = BukkitAdapter.adapt(player.getWorld());
            var region = new ProtectedCuboidRegion(cluster.boundingBox().id(),
                    cluster.boundingBox().min().toBlockVector3(world.getMinY()),
                    cluster.boundingBox().max().toBlockVector3(world.getMaxY()));
            region.getOwners().addPlayer(player.getUniqueId());
            worldGuard.getPlatform().getRegionContainer().get(world).addRegion(region);
        }
        configuration.save();
    }

    public static void draw(GridCluster cluster, Player player) {
        var min = cluster.boundingBox().min();
        var max = cluster.boundingBox().max();

        var y = player.getWorld().getHighestBlockYAt(BukkitAdapter.adapt(player.getWorld(), min.toBlockVector3()));

        cluster.updateMinHeight(y);

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

        } finally {
            WorldEdit.getInstance().getSessionManager().get(actor).remember(session);
        }

        MessageSender.getPluginMessageSender(GridSelector.class).sendMessage(player, "Created grid. Changed " + session.size() + " blocks.");
    }
}
