/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SerializableAs("gridSelectorGeneral")
public class General implements ConfigurationSerializable {
    private boolean createWorldGuardRegions = false;

    public General() {
    }

    public General(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        createWorldGuardRegions = map.getValueOrDefault("createWorldGuardRegions", false);
    }

    public boolean isCreateWorldGuardRegions() {
        return createWorldGuardRegions;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("createWorldGuardRegions", createWorldGuardRegions)
                .build();
    }
}
