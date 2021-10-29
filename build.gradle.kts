plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.0"
val publishData = PublishData(project)
val shadebase = "de.eldoria.gridselector.libs."

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "eldo-util", "1.10.11-DEV")
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.0.0-20211029.174513-2")
    compileOnly("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.6")
    compileOnly("com.plotsquared", "PlotSquared-Core", "6.1.2") // PlotSquared Core API
    compileOnly("com.plotsquared:PlotSquared-Bukkit:6.1.2") { isTransitive = false } // PlotSquared Bukkit API

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.6.0")
    testImplementation("org.spigotmc", "spigot-api", "1.16.5-R0.1-SNAPSHOT")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_16
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifact(tasks["jar"])
        artifact(tasks["shadowJar"])
        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])
        groupId = project.group as String?
        artifactId = project.name.toLowerCase()
        version = publishData.getVersion()
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            url = uri(publishData.getRepository())
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
        relocate("de.eldoria.eldoutilities", "de.eldoria.schematicbrush.libs.eldoutilities")
        mergeServiceFiles()
    }
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "version" to project.version
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
        from(shadowJar)
        destinationDir = File(path.toString())
    }
}

class PublishData(private val project: Project) {
    var type: Type = getReleaseType()
    var hashLength: Int = 7

    private fun getReleaseType(): Type {
        val branch = getCheckedOutBranch()
        return when {
            branch.contentEquals("master") -> Type.RELEASE
            branch.startsWith("dev") -> Type.DEV
            else -> Type.SNAPSHOT
        }
    }

    private fun getCheckedOutGitCommitHash(): String = System.getenv("GITHUB_SHA")?.substring(0, hashLength) ?: "local"

    private fun getCheckedOutBranch(): String = System.getenv("GITHUB_REF")?.replace("refs/heads/", "") ?: "local"

    fun getVersion(): String = getVersion(false)

    fun getVersion(appendCommit: Boolean): String =
        type.append(getVersionString(), appendCommit, getCheckedOutGitCommitHash())

    private fun getVersionString(): String = (project.version as String).replace("-SNAPSHOT", "").replace("-DEV", "")

    fun getRepository(): String = type.repo

    enum class Type(private val append: String, val repo: String, private val addCommit: Boolean) {
        RELEASE("", "https://eldonexus.de/repository/maven-releases/", false),
        DEV("-DEV", "https://eldonexus.de/repository/maven-dev/", true),
        SNAPSHOT("-SNAPSHOT", "https://eldonexus.de/repository/maven-snapshots/", true);

        fun append(name: String, appendCommit: Boolean, commitHash: String): String =
            name.plus(append).plus(if (appendCommit && addCommit) "-".plus(commitHash) else "")
    }
}
