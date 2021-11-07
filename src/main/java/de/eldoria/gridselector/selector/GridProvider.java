package de.eldoria.gridselector.selector;

import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.schematicbrush.brush.config.provider.SelectorProvider;
import de.eldoria.schematicbrush.brush.config.selector.Selector;
import de.eldoria.schematicbrush.schematics.SchematicRegistry;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GridProvider extends SelectorProvider {
    public GridProvider(SchematicRegistry registry) {
        super(Grid.class, "Grid", registry);
    }

    @Override
    public Selector parse(Arguments args) throws CommandException {
        return new Grid();
    }

    @Override
    public List<String> complete(Arguments args, Player player) {
        return Collections.emptyList();
    }

    @Override
    public Selector defaultSetting() {
        return new Grid();
    }

    @Override
    public String description() {
        return "Use the schematics you have selected with GridSelector previously.";
    }

    @Override
    public String permission() {
        return "gridselector.selector.use";
    }

    @Override
    public boolean hasArguments() {
        return false;
    }
}
