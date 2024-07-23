import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "com.gft.biometrics.framework"
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
    coordinates(MavenPublishConstants.groupId, "biometric-framework", MavenPublishConstants.version)

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


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(project(mapOf("path" to ":biometrics-domain")))

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
