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
    maven {
        name = "xesarConnect"
        url = uri("https://git.openforce.com/api/v4/projects/547/packages/maven")

            credentials(PasswordCredentials::class)
    }

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("com.open200:xesar-connect:1.1.0-rc.6")

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
