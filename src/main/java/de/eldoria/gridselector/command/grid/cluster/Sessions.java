/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.command.grid.cluster;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Direction;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.gridselector.config.elements.cluster.GridCluster;
import de.eldoria.gridselector.util.Colors;
import de.eldoria.messageblocker.blocker.MessageBlocker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sessions {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<UUID, GridCluster.Builder> sessions = new HashMap<>();
    private final MessageBlocker messageBlocker;
    private final BukkitAudiences audience;

    public Sessions(Plugin plugin, MessageBlocker messageBlocker) {
        this.messageBlocker = messageBlocker;
        audience = BukkitAudiences.builder(plugin).build();
    }

    public GridCluster.Builder getOrCreateSession(Player player) throws CommandException {
        if (!sessions.containsKey(player.getUniqueId())) {
            var direction = Direction.findClosest(BukkitAdapter.adapt(player).getLocation().getDirection(), Direction.Flag.CARDINAL);
            CommandAssertions.isTrue(direction != null, "Invalid direction. Please look in the requested direction");
            var builder = GridCluster.builder(BukkitAdapter.adapt(player.getLocation()), direction);
            sessions.put(player.getUniqueId(), builder);
        }
        return sessions.get(player.getUniqueId());
    }

    public void showBuilder(Player player) throws CommandException {
        messageBlocker.blockPlayer(player);
        var session = getOrCreateSession(player);
        var composer = MessageComposer.create()
                .text(session.asComponent());
        composer.newLine()
                .text("<%s><click:run_command:'/sbrg cluster draw'>[Create]</click>", Colors.ADD);
        composer.prependLines(20);
        messageBlocker.ifEnabled(composer, mess -> mess.newLine().text("<click:run_command:'/sbrg cluster close'><%s>[x]</click>", Colors.REMOVE));
        messageBlocker.announce(player, "[x]");
        audience.player(player).sendMessage(miniMessage.deserialize(composer.build()));
    }

    public void close(Player player) {
        sessions.remove(player.getUniqueId());
        messageBlocker.unblockPlayer(player);
    }
}
