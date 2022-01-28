/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.adapter.regionadapter;

import org.bukkit.Location;

import java.util.Optional;

public class ClusterWorldAdapter implements RegionAdapter{
    @Override
    public boolean isApplicable(Location location) {
        return false;
    }

    @Override
    public Optional<RegionResult> getRegion(Location location) {
        return Optional.empty();
    }
}
