package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.SchematicService;
import de.eldoria.gridselector.command.grid.DefineGrid;
import de.eldoria.gridselector.command.grid.Load;
import de.eldoria.gridselector.command.grid.Select;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin, SchematicBrushReborn reborn, SelectionListener selectionListener, SchematicService schematicService) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .withSubCommand(new DefineGrid(plugin))
                .withSubCommand(new Load(plugin, reborn, schematicService))
                .withSubCommand(new Select(plugin, selectionListener))
                .build());
    }
}
