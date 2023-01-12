plugins {
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("de.chojo.publishdata") version "1.0.9"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.1.5"
val shadebase = "de.eldoria." + rootProject.name + ".libs."

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.2.5")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.12")
    compileOnly("com.plotsquared", "PlotSquared-Core", "6.10.8") // PlotSquared Core API
    compileOnly("com.plotsquared", "PlotSquared-Bukkit", "6.10.8") { isTransitive = false } // PlotSquared Bukkit API
    compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.0.7")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.4.10") {
        exclude("com.intellectualsites.paster")
        exclude("org.yaml")
    }
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.4.10") {
        isTransitive = false
        exclude("org.yaml")
    }

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.1")
    testImplementation("com.sk89q.worldedit", "worldedit-bukkit", "7.2.12")
    testImplementation("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    testImplementation("de.eldoria", "eldo-util", "1.13.9")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            setUrl(publishData.getRepository())
            name = "EldoNexus"
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("de.eldoria.eldoutilities", "de.eldoria.schematicbrush.libs.eldoutilities")
        relocate("de.eldoria.messageblocker", "de.eldoria.schematicbrush.libs.messageblocker")
        relocate("net.kyori", "de.eldoria.schematicbrush.libs.kyori")
        mergeServiceFiles()
        archiveClassifier.set("all")
        archiveBaseName.set("GridSelector")
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "version" to publishData.getVersion(true)
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    register<Copy>("copyToServer") {
        val path = project.property("targetDir") ?: "";
        if (path.toString().isEmpty()) {
            println("targetDir is not set in gradle properties")
            return@register
        }
        println("Copying jar to $path")
        from(shadowJar)
        destinationDir = File(path.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}
