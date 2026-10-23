/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import org.bukkit.Bukkit;

import java.nio.file.Path;

public class General {
    private boolean createWorldGuardRegions = false;
    private String schematicPath = "SchematicBrushReborn/schematics";
    private boolean absolutePath = false;

    public General() {}

    public boolean isCreateWorldGuardRegions() {
        return createWorldGuardRegions;
    }

    public Path schematicPath() {
        if (absolutePath) {
            return Path.of(schematicPath);
        }
        return Bukkit.getUpdateFolderFile()
                .toPath()
                .toAbsolutePath()
                .getParent()
                .resolve(schematicPath);
    }
}
