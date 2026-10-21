/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import org.bukkit.Location;

import java.util.Optional;

public interface RegionAdapter {
    /**
     * Checks if a adapter is applicable to the world of the location
     *
     * @param location location to determine the world
     * @return true of the adapter is applicable to the world
     */
    boolean isApplicable(Location location);

    /**
     * Get the schematicRegion at a location.
     *
     * @param location location
     * @return the schematicRegion at the location of there is one
     */
    Optional<RegionResult> getRegion(Location location);
}
