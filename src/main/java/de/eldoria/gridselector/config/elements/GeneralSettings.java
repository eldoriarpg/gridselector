/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GeneralSettings implements ConfigurationSerializable {
    private Material highlight = Material.SEA_LANTERN;
    private boolean highlightBorder = false;
    private boolean highlightBounds = true;
    private boolean createWorldGuardRegions = false;

    public GeneralSettings() {
    }

    public GeneralSettings(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        highlight = map.getValueOrDefault("highlight", Material.SEA_LANTERN, Material.class);
        createWorldGuardRegions = map.getValueOrDefault("createWorldGuardRegions", false);
        highlightBounds = map.getValueOrDefault("highlightBounds", true);
        highlightBorder = map.getValueOrDefault("highlightBorder", false);
    }

    public Material highlight() {
        return highlight;
    }

    public boolean isCreateWorldGuardRegions() {
        return createWorldGuardRegions;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("highlight", highlight)
                .add("createWorldGuardRegions", createWorldGuardRegions)
                .add("highlightBorder", highlightBorder)
                .add("highlightBounds", highlightBounds)
                .build();
    }

    public boolean isHighlightBorder() {
        return highlightBorder;
    }

    public boolean isHighlightBounds() {
        return highlightBounds;
    }
}
