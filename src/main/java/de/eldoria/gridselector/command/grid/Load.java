package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.SchematicService;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematicbrush.brush.SchematicBrush;
import de.eldoria.schematicbrush.brush.config.BrushSettings;
import de.eldoria.schematicbrush.brush.config.SchematicSet;
import de.eldoria.schematicbrush.commands.parser.BrushSettingsParser;
import de.eldoria.schematicbrush.config.Config;
import de.eldoria.schematicbrush.config.sections.SchematicSource;
import de.eldoria.schematicbrush.schematics.Schematic;
import de.eldoria.schematicbrush.schematics.SchematicCache;
import de.eldoria.schematicbrush.util.WorldEditBrush;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Load extends AdvancedCommand implements IPlayerTabExecutor {
    private final SchematicBrushReborn reborn;
        private final SchematicService schematicService;

    public Load(Plugin plugin, SchematicBrushReborn reborn, SchematicService schematicService) {
        super(plugin, CommandMeta
                .builder("load")
                .build());
        this.reborn = reborn;
        this.schematicService = schematicService;
        if (reborn.config().getSchematicConfig().getSourceForPath(Path.of(plugin.getName())).isEmpty()) {
            reborn.config().getSchematicConfig().addSource(new SchematicSource(plugin.getName() + "/schematics", "grid", new ArrayList<>()));
            reborn.config().save();
        }
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {

        var optTempSettings = BrushSettingsParser.parseBrush(player, reborn.config(), reborn.schematics(), args.asArray());
        if(optTempSettings.isEmpty())return;
        var tempSettings = optTempSettings.get();
        var tempSet = tempSettings.schematicSets().get(0);

        var schematics = schematicService.loadPlayerSchematics(player);

        var schematicSet = new SchematicSet(schematics, tempSet.arguments(), tempSet.rotation(), tempSet.flip(), tempSet.weight());

        var settings = BrushSettings.newSingleBrushSettingsBuilder(schematicSet)
                .includeAir(tempSettings.isIncludeAir())
                .replaceAll(tempSettings.isReplaceAll())
                .withPlacementType(tempSettings.placement())
                .withYOffset(tempSettings.yOffset())
                .build();

        var schematicBrush = new SchematicBrush(plugin(), player, settings);
        if (WorldEditBrush.setBrush(player, schematicBrush)) {
            messageSender().sendMessage(player,
                    "Brush using " + settings.getSchematicCount() + " schematics created.");
        }
    }
}
