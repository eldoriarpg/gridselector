/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.schematics;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.eldoria.schematicbrush.brush.config.util.Nameable;
import de.eldoria.schematicbrush.schematics.Schematic;
import de.eldoria.schematicbrush.schematics.SchematicCache;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GridSchematics implements SchematicCache {
    public static final Nameable KEY = Nameable.of("grid");
    private final Plugin plugin;
    private final WorldEdit worldEdit = WorldEdit.getInstance();
    private final Map<UUID, Integer> directoryIds = new HashMap<>();

    public GridSchematics(Plugin plugin) {
        this.plugin = plugin;
    }

    public void saveRegions(Player player, List<CuboidRegion> regions) {
        var playerDirectory = getNextDirectory(player);
        var num = 0;
        for (var region : regions) {
            var clipboard = new BlockArrayClipboard(region);
            try (var session = worldEdit.newEditSession(BukkitAdapter.adapt(player.getWorld()))) {
                var copy = new ForwardExtentCopy(session, region, clipboard, region.getMinimumPoint());
                Operations.complete(copy);
                var schemFile = playerDirectory.resolve(Path.of(num + ".schem")).toFile();
                try (var writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schemFile))) {
                    writer.write(clipboard);
                }
            } catch (WorldEditException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save player schematic.", e);
            } catch (FileNotFoundException e) {
                plugin.getLogger().log(Level.SEVERE, "Schematic file not found.", e);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not write player schematic.", e);
            }
            num++;
        }
    }

    private Path getNextDirectory(Player player) {
        return getDirectory(player, getNextDirectoryId(player));
    }

    private Path getDirectory(Player player) {
        return getDirectory(player, getDirectoryId(player));
    }

    private Path getDirectory(Player player, int id) {
        return getDirectory(player.getUniqueId(), id);
    }

    private Path getDirectory(UUID uuid, int id) {
        var schematics = plugin.getDataFolder().toPath().resolve(Path.of("schematics", uuid.toString(), String.valueOf(id)));
        try {
            Files.createDirectories(schematics);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create player directory", e);
        }
        return schematics;
    }

    @Override
    public void init() {
        // Clear the schematic directory on startup
        clearSchematics();
    }

    @Override
    public void shutdown() {
        // Clear the schematic directory on shutdown
        clearSchematics();
    }

    private void clearSchematics() {
        try (var stream = Files.walk(plugin.getDataFolder().toPath().resolve(Path.of("schematics")))) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (FileNotFoundException e) {
            // ignore
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cloud not clear player directory", e);
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public Set<Schematic> getSchematicsByName(Player player, String name) {
        try (var stream = Files.walk(getDirectory(player))) {
            return stream.filter(Files::isRegularFile)
                    .map(f -> Schematic.of(f.toFile()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load player schematics");
        }
        return Collections.emptySet();
    }

    @Override
    public Set<Schematic> getSchematicsByDirectory(Player player, String name, String filter) {
        return getSchematicsByName(player, name);
    }

    @Override
    public List<String> getMatchingDirectories(Player player, String dir, int count) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getMatchingSchematics(Player player, String name, int count) {
        return Collections.emptyList();
    }

    @Override
    public int schematicCount() {
        return 0;
    }

    @Override
    public int directoryCount() {
        return 0;
    }


    private int getDirectoryId(Player uuid) {
        return directoryIds.computeIfAbsent(uuid.getUniqueId(), k -> 0);
    }

    private int getNextDirectoryId(Player uuid) {
        return directoryIds.compute(uuid.getUniqueId(), (k, v) -> v == null ? 0 : v + 1);
    }
}
