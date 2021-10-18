package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.listener.SelectionListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Select extends AdvancedCommand implements IPlayerTabExecutor {
    private final SelectionListener selectionListener;

    public Select(Plugin plugin, SelectionListener selectionListener) {
        super(plugin, CommandMeta
                .builder("select")
                .build());
        this.selectionListener = selectionListener;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        selectionListener.register(player);
    }
}
