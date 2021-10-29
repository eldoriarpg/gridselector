package de.eldoria.gridselector.listener;

import de.eldoria.gridselector.schematics.GridSchematics;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionListener implements Listener {
    private final Map<UUID, SelectionBrush> players = new HashMap<>();
    private final WorldAdapter adapter;
    private final GridSchematics schematicService;

    public SelectionListener(WorldAdapter adapter, GridSchematics schematicService) {
        this.adapter = adapter;
        this.schematicService = schematicService;
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

        if (brush.get().getRegions().isEmpty()) {
            player.sendMessage("You dont have any regions selected");
            return;
        }

        schematicService.saveRegions(player, brush.get().getRegions());

        brush.get().clearRegions();
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
}
