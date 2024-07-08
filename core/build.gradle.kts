import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "fr.codinbox.npclib"
version = "2.8.1"

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven(url = "https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven(url = "https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    implementation(project(":api"))
    implementation("commons-io:commons-io:2.11.0")
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

val targetJavaVersion = JavaVersion.VERSION_17
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
        mergeServiceFiles()
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
