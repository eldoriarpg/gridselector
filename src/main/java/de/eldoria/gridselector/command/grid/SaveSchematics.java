/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.util.Permissions;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class SaveSchematics extends AdvancedCommand implements IPlayerTabExecutor {
    private final SchematicBrushReborn schematicBrushReborn;
    private final GridSchematics gridSchematics;

    public SaveSchematics(Plugin plugin, SchematicBrushReborn schematicBrushReborn, GridSchematics gridSchematics) {
        super(plugin, CommandMeta.builder("saveSchematics")
                .addArgument("name", true)
                .withPermission(Permissions.SAVE)
                .build());
        this.schematicBrushReborn = schematicBrushReborn;
        this.gridSchematics = gridSchematics;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var schematics = gridSchematics.getSchematicsByName(player, null);

        var basePath = schematicBrushReborn.getDataFolder().toPath().resolve(Path.of("schematics"));

        if (!args.hasFlag("g")) {
            basePath = basePath.resolve(Path.of(player.getUniqueId().toString()));
        } else {
            CommandAssertions.permission(player, false, Permissions.SAVE_GLOBAL);
        }

        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            plugin().getLogger().log(Level.WARNING, "Could not create directories", e);
            messageSender().sendError(player, "Failed to save schematics. See console for further information.");
            return;
        }

        for (var schematic : schematics) {
            try {
                var target = basePath.resolve(args.asString(0) + "_" + schematic.getFile().getName());
                if (target.toFile().exists() && !args.hasFlag("f")) {
                    messageSender().sendError(player, "Name already in use.");
                    return;
                }
                Files.copy(schematic.getFile().toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                plugin().getLogger().log(Level.WARNING, "Could not save schematic", e);
                messageSender().sendError(player, "Failed to save schematics. See console for further information.");
                return;
            }
        }
        messageSender().sendMessage(player, "Saved " + schematics.size() + " schematics.");
    }
}
