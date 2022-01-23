package de.eldoria.gridselector.command.grid.cluster;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitItemCategoryRegistry;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.config.elements.GridCluster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Create extends AdvancedCommand implements IPlayerTabExecutor {
    public Create(Plugin plugin) {
        super(plugin, CommandMeta.builder("create")
                .addUnlocalizedArgument("size", true)
                .addUnlocalizedArgument("rows", true)
                .addUnlocalizedArgument("columns", true)
                .addUnlocalizedArgument("offset", true)
                .build());
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var direction = BukkitAdapter.adapt(player).getCardinalDirection();
        CommandAssertions.isTrue(direction != null, "Invalid view direction.");
        var builder = GridCluster.builder(BukkitAdapter.adapt(player.getLocation()), direction);

        builder.elementSize(args.get(0).asInt());
        builder.rows(args.get(1).asInt());
        builder.columns(args.get(2).asInt());
        builder.offset(args.get(3).asInt());

        var cluster = builder.build();

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {


        if (!args.flags().isEmpty()) {
            switch (args.flags().lastFlag()) {

            }
        }
        return Collections.emptyList();
    }
}
