import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "me.kshivang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")

    implementation("com.github.kittinunf.fuel:fuel:2.3.0")
    implementation("com.github.kittinunf.fuel:fuel-gson:2.3.0")

    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.13.0")

    implementation("it.skrape:skrapeit-core:1.0.0-alpha8")

}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}