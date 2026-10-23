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
import de.eldoria.gridselector.GridSelector;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.gridselector.util.Permissions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Export extends AdvancedCommand implements IPlayerTabExecutor {
    private GridSelector plugin;
    private final GridSchematics gridSchematics;

    public Export(GridSelector plugin, GridSchematics gridSchematics) {
        super(
                plugin,
                CommandMeta.builder("export")
                        .addArgument("name", true)
                        .withPermission(Permissions.Save.EXPORT)
                        .build());
        this.plugin = plugin;
        this.gridSchematics = gridSchematics;
    }

    /**
     * Copies the player's newest saved selection. Without {@code -f} nothing is copied if any target file exists.
     */
    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args)
            throws CommandException {
        var schematics = gridSchematics.getSchematicsByName(player, null);
        CommandAssertions.isFalse(schematics.isEmpty(), "You have no saved selection to export.");

        Path basePath;
        if (!args.flags().has("g")) {
            basePath = plugin.configuration()
                    .general()
                    .schematicPath()
                    .resolve(Path.of(player.getUniqueId().toString()));
        } else {
            CommandAssertions.permission(player, false, Permissions.Save.GLOBAL);
            basePath = plugin.configuration().general().schematicPath();
        }

        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            plugin().getLogger().log(Level.WARNING, "Could not create directories", e);
            messageSender().sendError(player, "Failed to save schematics. See console for further information.");
            return;
        }

        var name = args.asString(0);
        var overwrite = args.flags().has("f");
        Map<Path, Path> targets = schematics.stream()
                .map(schematic -> schematic.getFile().toPath())
                .collect(Collectors.toMap(
                        source -> source, source -> basePath.resolve(name + "_" + source.getFileName())));

        plugin.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!overwrite && targets.values().stream().anyMatch(Files::exists)) {
                messageSender().sendError(player, "Name already in use.");
                return;
            }
            try {
                for (var target : targets.entrySet()) {
                    Files.copy(target.getKey(), target.getValue(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                plugin().getLogger().log(Level.WARNING, "Could not save schematic", e);
                messageSender().sendError(player, "Failed to save schematics. See console for further information.");
                return;
            }
            messageSender().sendMessage(player, "Saved " + targets.size() + " schematics.");
        });
    }
}
