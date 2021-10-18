package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import org.bukkit.plugin.Plugin;

public class DefineGrid extends AdvancedCommand {
    public DefineGrid(Plugin plugin) {
        super(plugin, CommandMeta
                .builder("defineGrid")
                .addAlias("dg")
                .build());
    }
}
