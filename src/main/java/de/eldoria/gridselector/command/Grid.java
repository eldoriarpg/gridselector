package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.command.grid.DefineGrid;
import de.eldoria.gridselector.command.grid.Load;
import de.eldoria.gridselector.command.grid.Select;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .withSubCommand(new DefineGrid(plugin))
                .withSubCommand(new Load(plugin))
                .withSubCommand(new Select(plugin))
                .build());
    }
}
