package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.command.grid.DefineGrid;
import de.eldoria.gridselector.command.grid.DrawGrid;
import de.eldoria.gridselector.command.grid.Select;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.listener.SelectionListener;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin, SelectionListener selectionListener, Configuration config) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .withSubCommand(new DefineGrid(plugin, config))
                .withSubCommand(new Select(plugin, selectionListener))
                .withSubCommand(new DrawGrid(plugin, config))
                .build());
    }
}
