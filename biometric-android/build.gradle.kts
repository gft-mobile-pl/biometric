import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.gft.biometric"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    productFlavors {
        create("google") {
            dimension = "platform"
        }
        create("huawei") {
            dimension = "platform"
        }
    }

    flavorDimensions += listOf("platform")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

mavenPublishing {
    configure(
        AndroidMultiVariantLibrary(
            sourcesJar = true,
            publishJavadocJar = true,
        )
    )
    coordinates(project.property("libraryGroupId") as String, "biometric-android", project.property("libraryVersion") as String)

    pom {
        name.set(project.property("libraryNamePrefix") as String + " Android")
        description.set(project.property("libraryDescription") as String)
        inceptionYear.set(project.property("libraryInceptionYear") as String)
        url.set("https://${project.property("libraryRepositoryUrl") as String}")
        licenses {
            license {
                name.set(project.property("libraryLicenseName") as String)
                url.set(project.property("libraryLicenseUrl") as String)
                distribution.set(project.property("libraryLicenseDistribution") as String)
            }
        }
        developers {
            developer {
                name.set(project.property("libraryDeveloperName") as String)
            }
        }
        scm {
            url.set("https://${project.property("libraryRepositoryUrl") as String}")
            connection.set("scm:git:git://${project.property("libraryRepositoryUrl") as String}")
            developerConnection.set("scm:git:ssh://git@${project.property("libraryRepositoryUrl") as String}.git")
        }
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
    }
}


dependencies {
    api(project(":biometric-api"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
