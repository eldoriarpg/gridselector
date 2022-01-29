plugins {
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("de.chojo.publishdata") version "1.0.4"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.1.0"
val shadebase = "de.eldoria." + rootProject.name + ".libs."

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.1.0")
    compileOnly("de.eldoria", "eldo-util", "1.13.1-DEV")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.6")
    compileOnly("com.plotsquared", "PlotSquared-Core", "6.4.0") // PlotSquared Core API
    compileOnly("com.plotsquared:PlotSquared-Bukkit:6.4.0") { isTransitive = false } // PlotSquared Bukkit API
    compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.0.6")
    compileOnly("de.eldoria", "messageblocker", "1.0.3c-DEV")
    compileOnly("net.kyori", "adventure-platform-bukkit", "4.0.1")
    compileOnly("net.kyori", "adventure-text-minimessage", "4.10.0-SNAPSHOT")


    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.6.0")
    testImplementation("com.sk89q.worldedit", "worldedit-bukkit", "7.2.6")
    testImplementation("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    testImplementation("de.eldoria", "eldo-util", "1.13.0-DEV")
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
    hashLength = 7
    useEldoNexusRepos()
    publishTask("jar")
    publishTask("shadowJar")
    publishTask("sourcesJar")
    publishTask("javadocJar")
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

    compileTestJava{
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    shadowJar{
        relocate("de.eldoria.eldoutilities",  "de.eldoria.schematicbrush.libs.eldoutilities")
        relocate("de.eldoria.messageblocker", "de.eldoria.schematicbrush.libs.messageblocker")
        relocate("net.kyori", "de.eldoria.schematicbrush.libs.kyori")
        mergeServiceFiles()
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
}
