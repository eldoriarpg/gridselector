/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.gridselector.adapter.regionadapter.WorldAdapter;
import de.eldoria.gridselector.config.Configuration;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectionBrush implements Brush {
    private final MessageSender messageSender;
    private final Player owner;
    private final Map<String, MarkerResult> regions = new LinkedHashMap<>();
    private final WorldAdapter worldAdapter;
    private final Configuration configuration;

    public SelectionBrush(MessageSender messageSender, Player owner, WorldAdapter worldAdapter, Configuration configuration) {
        this.messageSender = messageSender;
        this.owner = owner;
        this.worldAdapter = worldAdapter;
        this.configuration = configuration;
    }

    @Override
    public void build(EditSession editSession, BlockVector3 position, Pattern pattern, double size) throws MaxChangedBlocksException {
        var optRegion = worldAdapter.getRegion(new com.sk89q.worldedit.util.Location(editSession.getWorld(), position.toVector3()));
        if (optRegion.isEmpty()) {
            messageSender.sendError(owner, "No grid or plot present.");
            return;
        }
        var result = optRegion.get();

        if (regions.containsKey(result.identifier())) {
            var marker = regions.remove(result.identifier());
            resolveMarker(marker, owner.getWorld());
            return;
        }

        var region = result.region();
        var yMax = reduceTop(editSession, region);
        var yMin = reduceFloor(editSession, region, result.worldHeight());
        var xMin = reduceEastWestSide(editSession, yMin, yMax, region, true);
        var xMax = reduceEastWestSide(editSession, yMin, yMax, region, false);
        var zMin = reduceSouthNorthSide(editSession, yMin, yMax, region, true);
        var zMax = reduceSouthNorthSide(editSession, yMin, yMax, region, false);

        var schemRegion = new CuboidRegion(editSession.getWorld(),
                BlockVector3.at(xMin, yMin, zMin),
                BlockVector3.at(xMax, yMax, zMax));
        var marker = new MarkerResult(result.identifier(), schemRegion, region, yMin);


        regions.put(result.identifier(), marker);

        var data = configuration.highlight().highlight().createBlockData();
        if (configuration.highlight().isHighlightBounds()) {
            for (var corner : marker.getCorners()) {
                owner.sendBlockChange(corner.toLocation(owner.getWorld()), data);
            }
        }

        if (configuration.highlight().isHighlightBorder()) {
            for (var borderBlock : marker.getBorderBlocks()) {
                owner.sendBlockChange(borderBlock.toLocation(owner.getWorld()), data);
            }
        }
    }

    private int reduceFloor(EditSession session, CuboidRegion region, int minHeight) {
        var min = region.getMinimumPoint();
        var max = region.getMaximumPoint();
        for (var y = minHeight; y <= max.getY(); y++) {
            if (checkFlat(session, y, min, max, mat -> mat == Material.AIR)) {
                return y;
            }
        }
        return region.getMinimumY();
    }

    private int reduceTop(EditSession session, CuboidRegion region) {
        var min = region.getMinimumPoint();
        var max = region.getMaximumPoint();

        for (var y = max.getY(); y >= min.getY(); y--) {
            if (checkFlat(session, y, min, max, mat -> mat != Material.AIR)) {
                return y;
            }
        }
        return region.getMaximumY();
    }

    private boolean checkFlat(EditSession session, int y, BlockVector3 min, BlockVector3 max, Predicate<Material> check) {
        for (var x = min.getX(); x < max.getX(); x++) {
            for (var z = min.getZ(); z < max.getZ(); z++) {
                var material = session.getBlock(BlockVector3.at(x, y, z)).getBlockType();
                if (check.test(BukkitAdapter.adapt(material)))
                    return true;
            }
        }
        return false;
    }

    private int reduceEastWestSide(EditSession session, int yMin, int yMax, CuboidRegion region, boolean east) {
        var min = region.getMinimumPoint();
        var max = region.getMaximumPoint();
        for (var x = (east ? min : max).getX(); east ? x <= max.getX() : x >= min.getX(); x += east ? 1 : -1) {
            for (var y = yMin; y <= yMax; y++) {
                for (var z = min.getZ(); z < max.getZ(); z++) {
                    var material = session.getBlock(BlockVector3.at(x, y, z)).getBlockType();
                    if (BukkitAdapter.adapt(material) != Material.AIR) {
                        return x;
                    }
                }
            }
        }
        return (east ? min : max).getX();
    }

    private int reduceSouthNorthSide(EditSession session, int yMin, int yMax, CuboidRegion region, boolean south) {
        var min = region.getMinimumPoint();
        var max = region.getMaximumPoint();
        for (var z = (south ? min : max).getZ(); south ? z <= max.getZ() : z >= min.getZ(); z += south ? 1 : -1) {
            for (var y = yMin; y <= yMax; y++) {
                for (var x = min.getX(); x < max.getX(); x++) {
                    var material = session.getBlock(BlockVector3.at(x, y, z)).getBlockType();
                    if (BukkitAdapter.adapt(material) != Material.AIR) {
                        return z;
                    }
                }
            }
        }
        return (south ? min : max).getZ();
    }

    public List<CuboidRegion> getRegions() {
        return regions.values().stream().map(MarkerResult::schematicRegion).collect(Collectors.toList());
    }

    private void clearMarker() {
        var world = owner.getWorld();
        for (var region : regions.values()) {
            resolveMarker(region, world);
        }
    }

    private void resolveMarker(MarkerResult region, World world) {
        for (var corner : region.getCorners()) {
            var loc = corner.toLocation(world);
            owner.sendBlockChange(loc, world.getBlockAt(loc).getBlockData());
        }
        for (var borderBlock : region.getBorderBlocks()) {
            var loc = borderBlock.toLocation(owner.getWorld());
            owner.sendBlockChange(loc, world.getBlockAt(loc).getBlockData());
        }

    }

    public void clearRegions() {
        clearMarker();
        regions.clear();
    }
}
