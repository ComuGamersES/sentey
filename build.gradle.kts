import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // dependencies that are only needed during build time
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    // dependencies that should be shaded into the final JAR file
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("team.unnamed:inject:1.0.1")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT")

    // dependencies that are needed during test time
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "comugamersRepository"
            credentials(PasswordCredentials::class)

            if (project.version.toString().contains("-SNAPSHOT")) {
                url = uri("https://repo.comugamers.com/repository/maven-snapshots/")
            } else {
                url = uri("https://repo.comugamers.com/repository/maven-releases/")
            }
        }
    }
}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.compilerArgs.add("-parameters")
    }

    getByName<Test>("test") {
        useJUnitPlatform()
    }

    named("build") {
        dependsOn(named("shadowJar"))
    }

    withType<ShadowJar> {
        val pkg = "com.comugamers.sentey.internal"
        val version = project.version.toString()

        relocate("dev.triumphteam.cmd", "$pkg.triumphteam.cmd")
        relocate("org.apache.http", "$pkg.apache.http")
        relocate("org.apache.commons.logging", "$pkg.apache.commons.logging")
        relocate("org.apache.commons.codec", "$pkg.apache.commons.codec")
        relocate("javax.inject", "$pkg.javax.inject")
        relocate("team.unnamed.inject", "$pkg.trew")
        relocate("org.bstats", "$pkg.bstats")

        archiveFileName.set("sentey-$version.jar")
    }
}

bukkit {
    main = "com.comugamers.sentey.Sentey"
    apiVersion = "1.13"
    version = "${project.version}"
    authors = listOf("Pabszito", "Jojo1545")
    description = "${findProperty("plugin-description")}"
    name = "${findProperty("plugin-name")}"
    website = "www.comugamers.com"
    softDepend = listOf("CG-Core")
}