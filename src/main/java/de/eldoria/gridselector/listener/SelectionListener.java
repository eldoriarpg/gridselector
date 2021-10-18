package de.eldoria.gridselector.listener;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import de.eldoria.gridselector.GridSelector;
import de.eldoria.gridselector.adapter.WorldAdapter;
import de.eldoria.gridselector.brush.SelectionBrush;
import de.eldoria.schematicbrush.util.WorldEditBrush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SelectionListener implements Listener {
    private final Map<UUID, SelectionBrush> players = new HashMap<>();
    private final Plugin plugin;
    private final WorldAdapter adapter;
    private final WorldEdit worldEdit = WorldEdit.getInstance();

    public SelectionListener(Plugin plugin, WorldAdapter adapter) {
        this.plugin = plugin;
        this.adapter = adapter;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            if (players.containsKey(event.getPlayer().getUniqueId())) {
                players.get(event.getPlayer().getUniqueId()).clearRegions();
                event.getPlayer().sendMessage("You changed the world. All selections will be removed.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (!players.containsKey(player.getUniqueId())) return;

        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        var brush = WorldEditBrush.getBrush(player, player.getInventory().getItemInMainHand().getType(), SelectionBrush.class);
        if (brush.isEmpty()) return;
        clearPlayerDirectory(player);
        var playerDirectory = getPlayerDirectory(player);
        var i = 0;
        for (var region : brush.get().getRegions()) {
            var clipboard = new BlockArrayClipboard(region);
            try (var session = worldEdit.newEditSession(BukkitAdapter.adapt(player.getWorld()))) {
                var copy = new ForwardExtentCopy(session, region, clipboard, region.getMinimumPoint());
                Operations.complete(copy);
                var schemFile = playerDirectory.resolve(Path.of(i + ".schem")).toFile();
                try (var writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schemFile))) {
                    writer.write(clipboard);
                } catch (IOException e) {
                    GridSelector.logger().log(Level.SEVERE, "Could not write player schematic.", e);
                }
            } catch (WorldEditException e) {
                GridSelector.logger().log(Level.SEVERE, "Could not save player schematic.", e);
            }
            i++;
        }
        brush.get().clearMarker();
        player.sendMessage("Selections saved. You can use them as a brush with /sbrg load");
        event.setCancelled(true);
    }

    public void register(Player player) {
        var brush = new SelectionBrush(player, adapter);
        if (!WorldEditBrush.setBrush(player, brush)) {
            return;
        }
        players.put(player.getUniqueId(), brush);
        player.sendMessage("Selection tool bound. Select schematics with right clicks. Left click when you are done.");
    }

    private Path getPlayerDirectory(Player player) {
        var schematics = plugin.getDataFolder().toPath().resolve(Path.of("schematics", player.getUniqueId().toString()));
        try {
            Files.createDirectories(schematics);
        } catch (IOException e) {
            GridSelector.logger().log(Level.SEVERE, "Failed to create player directory", e);
        }
        return schematics;
    }

    private void clearPlayerDirectory(Player player) {
        try {
            Files.walk(getPlayerDirectory(player))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (FileNotFoundException e) {
            // directory does not exist. everything is fine
        } catch (IOException e) {
            GridSelector.logger().log(Level.SEVERE, "Cloud not clear player directory", e);

        }
    }
}
