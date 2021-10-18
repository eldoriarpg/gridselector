package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import org.bukkit.plugin.Plugin;

public class Select extends AdvancedCommand {
    public Select(Plugin plugin) {
        super(plugin, CommandMeta
                .builder("select")
                .build());
    }
}
