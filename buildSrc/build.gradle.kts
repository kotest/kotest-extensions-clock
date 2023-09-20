import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
   mavenCentral()
}

plugins {
   `kotlin-dsl`
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
   kotlinOptions.jvmTarget = "11"
}

java {
   sourceCompatibility = JavaVersion.VERSION_11
   targetCompatibility = JavaVersion.VERSION_11
}
