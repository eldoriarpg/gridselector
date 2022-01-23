package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.gridselector.command.grid.cluster.Create;
import org.bukkit.plugin.Plugin;

public class Cluster extends AdvancedCommand {
    public Cluster(Plugin plugin) {
        super(plugin, CommandMeta.builder("cluster")
                .withSubCommand(new Create(plugin))
                .build());
    }
}
