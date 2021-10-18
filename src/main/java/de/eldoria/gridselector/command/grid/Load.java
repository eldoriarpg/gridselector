package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import org.bukkit.plugin.Plugin;

public class Load extends AdvancedCommand {
    public Load(Plugin plugin) {
        super(plugin, CommandMeta
                .builder("load")
                .build());
    }
}
