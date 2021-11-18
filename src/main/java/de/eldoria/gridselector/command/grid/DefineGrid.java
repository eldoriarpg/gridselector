package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.simplecommands.TabCompleteUtil;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class DefineGrid extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration config;
    private final RegionAdapter plotWorldAdapter;

    public DefineGrid(Plugin plugin, Configuration config, RegionAdapter plotWorldAdapter) {
        super(plugin, CommandMeta
                .builder("defineGrid")
                .addUnlocalizedArgument("size", true)
                .withPermission(Permissions.GRID_DEFINE)
                .build());
        this.config = config;
        this.plotWorldAdapter = plotWorldAdapter;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        CommandAssertions.isFalse(plotWorldAdapter.isApplicable(player.getLocation()), "error.gridInPlot");

        var gridWorld = config.createGridWorld(player.getWorld());
        gridWorld.gridSize(args.asInt(0));
        CommandAssertions.range(args.asInt(0), 2, 500);
        config.save();
        messageSender().sendMessage(player, "Set grid size");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        if (args.size() == 1) {
            return TabCompleteUtil.completeInt(args.asString(0), 2, 500, localizer());
        }
        return Collections.emptyList();
    }
}
