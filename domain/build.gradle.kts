plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

tasks.jar {
    from(sourceSets.main.get().allSource)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
