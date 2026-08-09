import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.file.DuplicatesStrategy

plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "9.6.1"
}

group = "fr.codinbox.npclib"
version = "4.0.0"

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven(url = "https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    implementation(project(":api"))
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    compileOnly("io.papermc.paper:paper-api:26.2.build.111-stable")
    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")
    testImplementation("io.papermc.paper:paper-api:26.2.build.111-stable")
    testImplementation("com.github.retrooper:packetevents-spigot:2.13.0")
}

val targetJavaVersion = JavaVersion.VERSION_25
java {
    sourceCompatibility = targetJavaVersion
    targetCompatibility = targetJavaVersion
    if (JavaVersion.current() < targetJavaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion.majorVersion))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    if (targetJavaVersion >= JavaVersion.VERSION_1_10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion.majorVersion.toInt()) // The string represent a number, like "1" for Java1
    }
    options.encoding = Charsets.UTF_8.name()
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.processResources.configure {
    // Define properties
    val props = mapOf(Pair("version", version))

    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("npclib")
        filesMatching("META-INF/services/**") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
        mergeServiceFiles()
        relocate("com.fasterxml.jackson", "fr.codinbox.npclib.libs.jackson")
    }
}

tasks {
    build {
        dependsOn("shadowJar")
    }
    test {
        useJUnitPlatform()
    }
}
