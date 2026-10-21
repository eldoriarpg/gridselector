import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.FALSE
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP

plugins {
    java
    `maven-publish`
    alias(libs.plugins.spotless)
    alias(libs.plugins.publishdata)
    alias(libs.plugins.pluginyml.bukkit)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runserver)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
    withJavadocJar()
}

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        target("**/*.java")
    }
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.schematicbrushreborn.api)
    compileOnly(libs.bundles.utilities)
    compileOnly(platform(libs.jackson.bom))
    compileOnly(libs.bundles.jackson)
    compileOnly(libs.fawe.core) {
        exclude("com.intellectualsites.paster")
        exclude("net.kyori")
    }
    compileOnly(libs.fawe.bukkit) {
        exclude("net.kyori")
    }
    compileOnly(libs.plotsquared.core) {
        isTransitive = false
    }
    compileOnly(libs.plotsquared.bukkit) {
        isTransitive = false
    }
    compileOnly(libs.guice) {
        isTransitive = false
    }
    compileOnly(libs.worldguard.bukkit) {
        exclude("com.sk89q.worldedit")
    }

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.paper)
    testImplementation(libs.bundles.utilities)
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.bundles.jackson)
    testImplementation(libs.fawe.core) {
        exclude("com.intellectualsites.paster")
        exclude("net.kyori")
    }
}

publishData {
    eldoNexus(dev = false)
    component = "shadow"
    buildData {
        put("lynaProduct", "8")
    }
}

val shadebase = "de.eldoria.schematicbrush.libs."

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    test {
        dependsOn(spotlessCheck)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("com.fasterxml", shadebase + "fasterxml")
        relocate("tools.jackson", shadebase + "jackson")
        relocate("dev.chojo.ocular", shadebase + "ocular")
        relocate("de.eldoria.jacksonbukkit", shadebase + "jacksonbukkit")
        relocate("de.eldoria.eldoutilities", shadebase + "utilities")
        mergeServiceFiles()
        addMultiReleaseAttribute = false
        exclude("META-INF/versions/**")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.3")
        downloadPlugins {
            modrinth("fastasyncworldedit", "2.16.1")
            modrinth("luckperms", "v5.5.71-bukkit")
            // TODO: download Schematic Brush Reborn 3 once it has a stable download URL
        }

        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}

bukkit {
    name = "GridSelector"
    main = "de.eldoria.gridselector.GridSelector"
    apiVersion = "1.21.11"
    version = project.version.toString()
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn", "FastAsyncWorldEdit")
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
        register("gridselector.updates") {
            description = "Hear about new versions on join and download them"
            default = OP
        }
    }
}
