package de.eldoria.gridselector.command.grid;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.RegionOperationException;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.simplecommands.TabCompleteUtil;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.config.Configuration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class DrawGrid extends AdvancedCommand implements IPlayerTabExecutor {
    private final WorldEdit worldEdit = WorldEdit.getInstance();
    private final Configuration config;
    private final RegionAdapter plotWorldAdapter;

    public DrawGrid(Plugin plugin, Configuration config, RegionAdapter plotWorldAdapter) {
        super(plugin, CommandMeta.builder("drawgrid")
                .addUnlocalizedArgument("range", true)
                .addUnlocalizedArgument("material", true)
                .addUnlocalizedArgument("material", true)
                .build());
        this.config = config;
        this.plotWorldAdapter = plotWorldAdapter;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        CommandAssertions.isFalse(plotWorldAdapter.isApplicable(player.getLocation()), "error.gridInPlot");

        var world = BukkitAdapter.adapt(player.getWorld());
        var actor = BukkitAdapter.adapt(player);
        var session = worldEdit.newEditSessionBuilder().actor(actor).world(world).build();
        try (session ) {
            var loc = player.getLocation();
            var floor = session.getHighestTerrainBlock(loc.getBlockX(), loc.getBlockZ(), player.getWorld().getMinHeight(), player.getWorld().getMaxHeight());

            var optGridWorld = config.getGridWorld(player.getWorld());

            CommandAssertions.isFalse(optGridWorld.isEmpty(), "This is not a grid world.");

            var gridWorld = optGridWorld.get();

            var baseRegion = gridWorld.getGridElement(player.getLocation());

            var range = args.asInt(0);

            baseRegion.shift(BlockVector3.at(-range, 0, -range));

            var first = BukkitAdapter.adapt(args.asMaterial(1).createBlockData());
            var second = BukkitAdapter.adapt(args.asMaterial(2).createBlockData());

            var minP = baseRegion.getMinimumPoint();

            for (var x = minP.getBlockX(); x < minP.getBlockX() + range * 2; x += gridWorld.gridSize()) {
                for (var z = minP.getBlockZ(); z < minP.getBlockZ() + range * 2; z += gridWorld.gridSize()) {
                    var currLoc = new Location(player.getWorld(), x, 0, z);
                    var currCube = gridWorld.getGridElement(currLoc);
                    var min = currCube.getMinimumPoint();
                    var max = currCube.getMaximumPoint();
                    var region = new CuboidRegion(
                            BlockVector3.at(min.getBlockX(), floor, min.getBlockZ()),
                            BlockVector3.at(max.getBlockX(), floor, max.getBlockZ()));
                    session.setBlocks(region, gridWorld.getElementMaterial(currLoc, first, second));
                }
            }
        } catch (RegionOperationException e) {
            plugin().getLogger().log(Level.SEVERE, "Operation failed", e);
            throw CommandException.message("Operation failed.");
        } catch (MaxChangedBlocksException e) {
            throw CommandException.message("Operation too large");
        }
        worldEdit.getSessionManager().get(actor).remember(session);

        messageSender().sendMessage(player, "Grid drawn.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return switch (args.size()) {
            case 1 -> TabCompleteUtil.completeInt(args.asString(0), 0, 500, localizer());
            case 2 -> TabCompleteUtil.completeMaterial(args.asString(1), true);
            case 3 -> TabCompleteUtil.completeMaterial(args.asString(2), true);
            default -> Collections.emptyList();
        };
    }
}
