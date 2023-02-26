plugins {
    id("java")
}

group = "fr.codinbox.npclib"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven(url = "https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven(url = "https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}
