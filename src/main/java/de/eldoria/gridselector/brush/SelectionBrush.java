package de.eldoria.gridselector.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.gridselector.adapter.WorldAdapter;
import de.eldoria.gridselector.adapter.regionadapter.RegionResult;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectionBrush implements Brush {
    private final MessageSender messageSender;
    private final Player owner;
    private final Map<String, RegionResult> regions = new HashMap<>();
    private final WorldAdapter worldAdapter;

    public SelectionBrush(MessageSender messageSender, Player owner, WorldAdapter worldAdapter) {
        this.messageSender = messageSender;
        this.owner = owner;
        this.worldAdapter = worldAdapter;
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
            for (var corner : regions.remove(result.identifier()).getCorners()) {
                var block = editSession.getBlock(BlockVector3.at(corner.getX(), corner.getY(), corner.getZ()));
                owner.sendBlockChange(corner.toLocation(owner.getWorld()), BukkitAdapter.adapt(block));
            }
            return;
        }

        var region = result.region();
        var yMax = reduceTop(editSession, region);
        var yMin = reduceFloor(editSession, region);
        var xMin = reduceEastWestSide(editSession, yMin, yMax, region, true);
        var xMax = reduceEastWestSide(editSession, yMin, yMax, region, false);
        var zMin = reduceSouthNorthSide(editSession, yMin, yMax, region, true);
        var zMax = reduceSouthNorthSide(editSession, yMin, yMax, region, false);

        region = new CuboidRegion(editSession.getWorld(),
                BlockVector3.at(xMin, yMin, zMin),
                BlockVector3.at(xMax, yMax, zMax));
        result = new RegionResult(result.identifier(), region);


        regions.put(result.identifier(), result);

        var data = Material.SEA_LANTERN.createBlockData();
        for (var corner : result.getCorners()) {
            owner.sendBlockChange(corner.toLocation(owner.getWorld()), data);
        }
    }

    public int reduceFloor(EditSession session, CuboidRegion region) {
        var min = region.getMinimumPoint();
        var max = region.getMaximumPoint();
        for (var y = min.getY(); y <= max.getY(); y++) {
            if (checkFlat(session, y, min, max, m -> m == Material.AIR)) {
                return y;
            }
        }
        return region.getMinimumY();
    }

    public int reduceTop(EditSession session, CuboidRegion region) {
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
        return regions.values().stream().map(RegionResult::region).collect(Collectors.toList());
    }

    public void clearMarker() {
        var world = owner.getWorld();
        for (var region : regions.values()) {
            for (var corner : region.getCorners()) {
                var loc = corner.toLocation(world);
                owner.sendBlockChange(loc, world.getBlockAt(loc).getBlockData());
            }
        }
    }

    public void clearRegions() {
        clearMarker();
        regions.clear();
    }
}
