package de.eldoria.gridselector.command;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.adapter.regionadapter.RegionAdapter;
import de.eldoria.gridselector.command.grid.DefineGrid;
import de.eldoria.gridselector.command.grid.DrawGrid;
import de.eldoria.gridselector.command.grid.SaveSchematics;
import de.eldoria.gridselector.command.grid.Select;
import de.eldoria.gridselector.config.Configuration;
import de.eldoria.gridselector.listener.SelectionListener;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.plugin.Plugin;

public class Grid extends AdvancedCommand {
    public Grid(Plugin plugin, SchematicBrushReborn schematicBrushReborn, SelectionListener selectionListener, Configuration config, GridSchematics gridSchematics, RegionAdapter plotWorldAdapter) {
        super(plugin, CommandMeta.builder("schematicbrushgrid")
                .withSubCommand(new DefineGrid(plugin, config, plotWorldAdapter))
                .withSubCommand(new Select(plugin, selectionListener))
                .withSubCommand(new DrawGrid(plugin, config, plotWorldAdapter))
                .withSubCommand(new SaveSchematics(plugin, schematicBrushReborn, gridSchematics))
                .build());
    }
}
