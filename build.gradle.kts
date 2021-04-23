import net.minecraftforge.gradle.userdev.UserDevExtension
import org.spongepowered.asm.gradle.plugins.MixinExtension

val modGroup: String by extra
val modVersion: String by extra

group = modGroup
version = modVersion

buildscript {
    repositories {
        jcenter()
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:4.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    java
    kotlin("jvm")
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("org.spongepowered.mixin")
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
	maven("https://impactdevelopment.github.io/maven/")
    maven("https://jitpack.io")
}

val library by configurations.creating {}

dependencies {
    val kotlinVersion: String by project
    val kotlinxCoroutineVersion: String by project

    fun minecraft(dependencyNotation: Any): Dependency? =
        "minecraft"(dependencyNotation)

    fun ModuleDependency.exclude(moduleName: String) =
        exclude(mapOf("module" to moduleName))

    library(kotlin("stdlib", kotlinVersion))
    library(kotlin("reflect", kotlinVersion))
    library(kotlin("stdlib-jdk8", kotlinVersion))

    // This Baritone will NOT be included in the jar
    implementation("com.github.cabaletta:baritone:1.2.14")
	library("cabaletta:baritone-api:1.2")

    library("club.minnced:java-discord-rpc:2.0.2") {
        exclude("jna")
    }

	library("org.reflections:reflections:0.9.11"){
        exclude("guava")
    }

    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

    minecraft("net.minecraftforge:forge:1.12.2-14.23.5.2854")

    library("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        exclude("commons-io")
        exclude("gson")
        exclude("guava")
        exclude("launchwrapper")
        exclude("log4j-core")
    }

    annotationProcessor("org.spongepowered:mixin:0.8.2:processor") {
        exclude("gson")
    }

    implementation(library)
}

configure<MixinExtension> {
    defaultObfuscationEnv = "searge"
    add(sourceSets["main"], "mixins.deneb.refmap.json")
}

configure<UserDevExtension> {
    mappings(
        mapOf(
            "channel" to "stable",
            "version" to "39-1.12"
        )
    )

    accessTransformers (file("src/main/resources/deneb_at.cfg"))

    runs {
        create("client") {
            workingDirectory = project.file("run").path

            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP",
                    "forge.logging.console.level" to "info",
                    "fml.coreMods.load" to "club.deneb.client.mixin.MixinLoader",
                    "mixin.env.disableRefMap" to "true"
                )
            )
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            useIR = true
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xinline-classes")
        }
    }

    processResources {
        inputs.property("version", version)

        from(project.the<SourceSetContainer>()["main"].resources.srcDirs) {
            include("mcmod.info")
            include("mixins.deneb.json")
            expand("version" to version)
        }

        rename("(.+_at.cfg)", "META-INF/$1")
    }

    jar {
        manifest {
            attributes(
                "FMLCorePluginContainsFMLMod" to "true",
                "FMLCorePlugin" to "club.deneb.client.mixin.MixinLoader",
                "MixinConfigs" to "mixins.deneb.json",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "TweakOrder" to 0,
                "ForceLoadAsMod" to "true",
				"FMLAT" to "deneb_at.cfg"
            )
        }

        from(
            library.map {
                if (it.isDirectory) it
                else zipTree(it)
            }
        )
    }
}