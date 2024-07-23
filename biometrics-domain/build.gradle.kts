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
    coordinates(project.property("publishingGroupId") as String, "biometric-domain", project.property("publishingVersion") as String)

    pom {
        name.set(project.property("publishingLibraryName") as String)
        description.set(project.property("publishingDescription") as String)
        inceptionYear.set(project.property("publishingInceptionYear") as String)
        url.set("https://${project.property("publishingRepositoryUrl") as String}")
        licenses {
            license {
                name.set(project.property("publishingLicenseName") as String)
                url.set(project.property("publishingLicenseUrl") as String)
                distribution.set(project.property("publishingLicenseDistribution") as String)
            }
        }
        developers {
            developer {
                name.set(project.property("publishingDeveloperName") as String)
            }
        }
        scm {
            url.set("https://${project.property("publishingRepositoryUrl") as String}")
            connection.set("scm:git:git://${project.property("publishingRepositoryUrl") as String}")
            developerConnection.set("scm:git:ssh://git@${project.property("publishingRepositoryUrl") as String}.git")
        }
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }
}
