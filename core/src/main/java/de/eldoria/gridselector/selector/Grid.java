/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.selector;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import de.eldoria.gridselector.schematics.GridSchematics;
import de.eldoria.schematicbrush.brush.config.selector.Selector;
import de.eldoria.schematicbrush.schematics.Schematic;
import de.eldoria.schematicbrush.schematics.SchematicRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class Grid implements Selector {

    public Grid(Map<String, Object> objectMap) {
    }

    public Grid() {
    }

    @Override
    public Set<Schematic> select(Player player, SchematicRegistry registry) {
        return registry.get(GridSchematics.KEY).getSchematicsByName(player, "");
    }

    @Override
    public String name() {
        return "Grid";
    }

    @Override
    public String descriptor() {
        return "";
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder().build();
    }
}
