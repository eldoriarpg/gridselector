import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.FALSE

plugins {
    alias(libs.plugins.pluginyml)
    alias(libs.plugins.publishdata)
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.7.1")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.3.6")
    // PlotSquared Core API
    compileOnly("com.plotsquared", "PlotSquared-Core", "6.11.1") {
        exclude("com.intellectualsites.paster", "Paster")
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("com.intellectualsites.informative-annotations", "informative-annotations")
    }
    compileOnly("com.plotsquared", "PlotSquared-Bukkit", "6.11.1") { isTransitive = false } // PlotSquared Bukkit API
    compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.0.9")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.9.2") {
        exclude("com.intellectualsites.paster")
        exclude("org.yaml")
    }
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.9.2") {
        isTransitive = false
        exclude("org.yaml")
    }

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.10.2")
    testImplementation("com.sk89q.worldedit", "worldedit-bukkit", "7.3.6")
    testImplementation("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    testImplementation("de.eldoria", "eldo-util", "1.14.4")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
}

publishData {
    useEldoNexusRepos()
}

bukkit {
    name = "GridSelector"
    main = "de.eldoria.gridselector.GridSelector"
    apiVersion = "1.16"
    version = publishData.getVersion(true)
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn")
    softDepend = listOf("PlotSquared", "WorldGuard")

    commands {
        register("schematicbrushgrid") {
            aliases = listOf("sbrg")
            permission = "gridselector.use"
        }
    }

    permissions {
        register("gridselector.export") {
            default = FALSE
        }
        register("gridselector.export.global") {
            default = FALSE
        }
        register("gridselector.cluster.create") {
            default = FALSE
        }
        register("gridselector.cluster.remove") {
            default = FALSE
        }
        register("gridselector.cluster.repair") {
            default = FALSE
        }
    }
}
