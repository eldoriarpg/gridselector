plugins {
    alias(libs.plugins.publishdata)
    alias(libs.plugins.shadow)
    `maven-publish`
    alias(libs.plugins.runserver)
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
        relocate("de.eldoria.messageblocker", shadebase + "messageblocker")
        relocate("com.jackson", shadebase + "jackson")
        relocate("de.eldoria.eldoutilities", shadebase + "utilities")
    }

    build {
        dependsOn(shadowJar)
    }

    register<Copy>("copyToServer") {
        val path = rootProject.property("targetDir") ?: "";
        if (path.toString().isEmpty()) {
            println("targetDir is not set in gradle properties")
            return@register
        }
        println("Copying jar to $path")
        from(shadowJar)
        into(path.toString())
        rename{"gridselector.jar"}
    }

            runServer {
        minecraftVersion("1.21.1")
        downloadPlugins {
            url("https://ci.athion.net/job/FastAsyncWorldEdit/lastSuccessfulBuild/artifact/artifacts/FastAsyncWorldEdit-Paper-2.12.4-SNAPSHOT-1013.jar")
            url("https://download.luckperms.net/1569/bukkit/loader/LuckPerms-Bukkit-5.4.152.jar")
        }

      jvmArgs("-Dcom.mojang.eula.agree=true")
    }

}
