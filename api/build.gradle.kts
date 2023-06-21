plugins {
    id("java")
    id("maven-publish")
}

group = "fr.codinbox.npclib"
version = "2.4.0.1"

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

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven(url = "https://git.codinbox.fr/api/v4/projects/29/packages/maven") {
            name = "npclib"
            credentials(HttpHeaderCredentials::class)
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

