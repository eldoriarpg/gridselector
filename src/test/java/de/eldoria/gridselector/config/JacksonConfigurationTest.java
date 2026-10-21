/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import com.sk89q.worldedit.math.BlockVector2;
import de.eldoria.gridselector.config.elements.cluster.Plot;
import dev.chojo.ocular.Configurations;
import dev.chojo.ocular.dataformats.YamlDataFormat;
import dev.chojo.ocular.key.Key;
import org.bukkit.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class JacksonConfigurationTest {
    private static final UUID WORLD = UUID.fromString("46be765d-9464-4335-9a08-d2377c5e187f");

    @TempDir
    Path dataFolder;

    @Test
    void readsClusterWorldsWrittenByPreviousVersion() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("clusterWorlds.yml")) {
            Files.copy(in, dataFolder.resolve("clusterWorlds.yml"));
        }

        var clusterWorld = configurations().secondary(JacksonConfiguration.CLUSTER_WORLDS).clusterWorlds().get(WORLD);

        Assertions.assertNotNull(clusterWorld);
        Assertions.assertEquals(WORLD, clusterWorld.uid());
        var cluster = clusterWorld.getCluster(1).orElseThrow();
        Assertions.assertEquals(Plot.of(74, -27, 92, -9), cluster.plot());
        Assertions.assertEquals(63, cluster.minHeight());
        Assertions.assertEquals(Material.RED_CONCRETE, cluster.borderMaterial());
        Assertions.assertTrue(cluster.contains(BlockVector2.at(80, -20)));
    }

    @Test
    void keepsClusterWorldsAcrossSaveAndLoad() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("clusterWorlds.yml")) {
            Files.copy(in, dataFolder.resolve("clusterWorlds.yml"));
        }
        var written = configurations();
        written.secondary(JacksonConfiguration.CLUSTER_WORLDS);
        written.save();

        var cluster = configurations().secondary(JacksonConfiguration.CLUSTER_WORLDS).clusterWorlds().get(WORLD)
                                      .getCluster(1).orElseThrow();

        Assertions.assertEquals(Plot.of(74, -27, 92, -9), cluster.plot());
        Assertions.assertEquals(Material.WHITE_CONCRETE, cluster.floorMaterial());
    }

    @Test
    void takesTheOldUpdateSwitchAsNotice() throws IOException {
        Files.writeString(dataFolder.resolve("config.yml"), "checkUpdates: false\n");

        var updates = configurations().main().updates();

        Assertions.assertFalse(updates.notice());
        Assertions.assertFalse(updates.autoUpdate());
        Assertions.assertTrue(updates.askForAutoUpdate());
    }

    @Test
    void dropsTheOldUpdateSwitchOnSave() throws IOException {
        Files.writeString(dataFolder.resolve("config.yml"), "checkUpdates: false\n");
        var written = configurations();
        written.main();
        written.save();

        var config = Files.readString(dataFolder.resolve("config.yml"));

        Assertions.assertFalse(config.contains("checkUpdates"));
        Assertions.assertFalse(configurations().main().updates().notice());
    }

    @Test
    void noticesUpdatesByDefault() {
        var updates = configurations().main().updates();

        Assertions.assertTrue(updates.notice());
        Assertions.assertFalse(updates.autoUpdate());
        Assertions.assertTrue(updates.askForAutoUpdate());
    }

    private Configurations<ConfigFile> configurations() {
        return new Configurations<>(dataFolder, Key.builder(Path.of("config.yml"), ConfigFile::new).build(),
                List.of(new YamlDataFormat()), getClass().getClassLoader(), null);
    }
}
