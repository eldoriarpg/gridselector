/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

@SerializableAs("gridSelectorGeneral")
public class General implements ConfigurationSerializable {
    private boolean createWorldGuardRegions = false;
    private String schematicPath = "SchematicBrushReborn/schematics";
    private boolean absolutePath = false;

    public General() {
    }

    public General(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        createWorldGuardRegions = map.getValueOrDefault("createWorldGuardRegions", false);
        schematicPath = map.getValueOrDefault("schematicPath", schematicPath);
        absolutePath = map.getValueOrDefault("absolutePath", absolutePath);
    }

    public boolean isCreateWorldGuardRegions() {
        return createWorldGuardRegions;
    }

    public Path schematicPath() {
        if (absolutePath) {
            return Path.of(schematicPath);
        }
        return Bukkit.getUpdateFolderFile().toPath().toAbsolutePath().getParent().resolve(schematicPath);
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                                .add("createWorldGuardRegions", createWorldGuardRegions)
                                .add("schematicPath", schematicPath)
                                .add("absolutePath", absolutePath)
                                .build();
    }
}
