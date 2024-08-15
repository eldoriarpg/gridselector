plugins {
    alias(libs.plugins.publishdata)
    alias(libs.plugins.shadow)
    `maven-publish`
}

dependencies {
    implementation(project(":core"))
}


publishData {
    addBuildData()
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
    shadowJar {
        val shadebase = "de.eldoria.schematicbrush.libs."
        relocate("org.bstats", shadebase + "bstats")
        relocate("de.eldoria.eldoutilities", shadebase + "eldoutilities")
        relocate("de.eldoria.jacksonbukkit", shadebase + "jacksonbukkit")
        relocate("de.eldoria.messageblocker", shadebase + "messageblocker")
        relocate("com.fasterxml", shadebase + "fasterxml")
        relocate("org.yaml", shadebase + "fasterxml")
        relocate("net.kyori", shadebase + "kyori")
        mergeServiceFiles()
        archiveVersion.set(rootProject.version as String)
    }

    build {
        dependsOn(shadowJar)
    }
}
