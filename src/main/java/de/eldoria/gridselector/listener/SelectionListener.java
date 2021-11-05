package de.eldoria.gridselector.listener;

import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.gridselector.adapter.WorldAdapter;
import de.eldoria.gridselector.brush.SelectionBrush;
import de.eldoria.gridselector.schematics.GridSchematics;
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
    private final MessageSender messageSender;

    public SelectionListener(WorldAdapter adapter, GridSchematics schematicService, MessageSender messageSender) {
        this.adapter = adapter;
        this.schematicService = schematicService;
        this.messageSender = messageSender;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            if (players.containsKey(event.getPlayer().getUniqueId())) {
                players.get(event.getPlayer().getUniqueId()).clearRegions();
                messageSender.sendError(event.getPlayer(), "You changed the world. All selections will be removed.");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (!players.containsKey(player.getUniqueId())) return;

        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        if (!(WorldEditBrush.getBrush(player) instanceof SelectionBrush brush)) {
            return;
        }

        if (brush.getRegions().isEmpty()) {
            messageSender.sendError(player, "You dont have any regions selected");
            return;
        }

        schematicService.saveRegions(player, brush.getRegions());

        brush.clearRegions();
        messageSender.sendMessage(player, "Selections saved. Use the grid Selector to use them.");
        event.setCancelled(true);
    }

    public void register(Player player) {
        var brush = new SelectionBrush(messageSender, player, adapter);
        if (!WorldEditBrush.setBrush(player, brush)) {
            return;
        }
        players.put(player.getUniqueId(), brush);
        messageSender.sendMessage(player, "Selection tool bound. Select schematics with right clicks. Left click when you are done.");
    }
}
