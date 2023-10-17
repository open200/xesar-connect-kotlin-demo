import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "com.open200"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("com.open200:xesar-connect:0.5.0")

    // add SLF4J implementation of your choice (e.g. logback)
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // add security provider of your choice (e.g. bouncycastle)
    implementation("org.bouncycastle:bcprov-jdk18on:1.76")

    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

application {
    mainClass.set("MainKt")
}
