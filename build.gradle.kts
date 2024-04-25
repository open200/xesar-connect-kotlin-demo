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
    mavenLocal()
    mavenCentral()
}

val kotlinxCoroutinesVersion: String = "1.8.0"
val kotlinLoggingVersion: String = "3.0.5"
val xesarConnectVersion: String = "1.0.0"
val bouncycastleVersion: String = "1.76"
val logbackVersion: String = "1.4.14"



dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("com.open200:xesar-connect:$xesarConnectVersion")

    // add SLF4J implementation of your choice (e.g. logback)
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // add security provider of your choice (e.g. bouncycastle)
    implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")

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
