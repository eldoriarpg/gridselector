/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SerializableAs("gridSelectorHighlight")
public class Highlight implements ConfigurationSerializable {
    private Material highlight = Material.SEA_LANTERN;
    private boolean highlightBorder = false;
    private boolean highlightBounds = true;

    public Highlight() {
    }

    public Highlight(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        highlight = map.getValueOrDefault("highlight", Material.SEA_LANTERN, Material.class);
        highlightBounds = map.getValueOrDefault("highlightBounds", true);
        highlightBorder = map.getValueOrDefault("highlightBorder", false);

    }

    /**
     * The material used to hightlight selections
     * @return material
     */
    public Material highlight() {
        return highlight;
    }

    /**
     * Defines whether plot borders should be highlighted
     * @return true if they should be highlighted
     */
    public boolean isHighlightBorder() {
        return highlightBorder;
    }

    /**
     * Defines whether region bounds should be highlighted
     * @return true if they should be highlighted
     */
    public boolean isHighlightBounds() {
        return highlightBounds;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("highlight", highlight)
                .add("highlightBorder", highlightBorder)
                .add("highlightBounds", highlightBounds)
                .build();
    }
}
