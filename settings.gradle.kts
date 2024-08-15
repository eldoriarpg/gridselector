rootProject.name = "gridselector"
include("core")
include("legacy")
include("latest")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven {
            name = "EldoNexus"
            url = uri("https://eldonexus.de/repository/maven-public/")

        }
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("publishdata", "de.chojo.publishdata").version("1.4.0")
            plugin("licenser", "org.cadixdev.licenser").version("0.6.1")
            plugin("shadow", "io.github.goooler.shadow").version("8.1.8")
            plugin("pluginyml", "net.minecrell.plugin-yml.bukkit").version("0.6.0")
        }
    }
}
