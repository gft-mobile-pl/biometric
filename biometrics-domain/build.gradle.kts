import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.vanniktech.maven.publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


mavenPublishing {
    coordinates(MavenPublishConstants.groupId, "biometric-domain", MavenPublishConstants.version)

    pom {
        name.set(MavenPublishConstants.libraryName)
        description.set(MavenPublishConstants.description)
        inceptionYear.set(MavenPublishConstants.inceptionYear)
        url.set("https://${MavenPublishConstants.repositoryUrl}")
        licenses {
            license {
                name.set(MavenPublishConstants.licenseName)
                url.set(MavenPublishConstants.licenseUrl)
                distribution.set(MavenPublishConstants.licenseDistribution)
            }
        }
        developers {
            developer {
                name.set(MavenPublishConstants.developerName)
            }
        }
        scm {
            url.set("https://${MavenPublishConstants.repositoryUrl}")
            connection.set("scm:git:git://${MavenPublishConstants.repositoryUrl}")
            developerConnection.set("scm:git:ssh://git@${MavenPublishConstants.repositoryUrl}.git")
        }
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }
}
