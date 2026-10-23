/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import org.bukkit.Material;

public class Highlight {
    private Material highlight = Material.SEA_LANTERN;
    private boolean highlightBorder = false;
    private boolean highlightBounds = true;

    public Highlight() {}

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
}
