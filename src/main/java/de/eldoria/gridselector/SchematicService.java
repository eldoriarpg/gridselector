package de.eldoria.gridselector;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.eldoria.schematicbrush.schematics.Schematic;
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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SchematicService {
    private final Plugin plugin;
    private final WorldEdit worldEdit = WorldEdit.getInstance();

    public SchematicService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void saveRegions(Player player, List<CuboidRegion> regions) {
        clearPlayerDirectory(player);
        var playerDirectory = getPlayerDirectory(player);
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

    private Path getPlayerDirectory(Player player) {
        var schematics = plugin.getDataFolder().toPath().resolve(Path.of("schematics", player.getUniqueId().toString()));
        try {
            Files.createDirectories(schematics);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create player directory", e);
        }
        return schematics;
    }

    private void clearPlayerDirectory(Player player) {
        try (var stream = Files.walk(getPlayerDirectory(player))) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (FileNotFoundException e) {
            // directory does not exist. everything is fine
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cloud not clear player directory", e);

        }
    }

    public Set<Schematic> loadPlayerSchematics(Player player) {
        try(var stream = Files.walk(getPlayerDirectory(player))){
            return stream.filter(Files::isRegularFile)
                    .map(f -> new Schematic(ClipboardFormats.findByFile(f.toFile()), f.toFile()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load player schematics");
        }
        return Collections.emptySet();
    }
}
