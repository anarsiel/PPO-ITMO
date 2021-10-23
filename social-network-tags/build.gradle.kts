import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "me.admin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20210307")
    implementation("junit:junit:4.13.1")

    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.assertj:assertj-core:3.6.1")
    implementation("com.xebialabs.restito:restito:0.9.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}