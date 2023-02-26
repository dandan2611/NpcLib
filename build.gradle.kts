plugins {
    java
    `maven-publish`
}

group = "fr.codinbox.npclib"
version = "1.0.0-SNAPSHOT"

repositories {
}

dependencies {
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
