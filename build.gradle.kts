import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
   id("kotest-publishing-conventions")
   kotlin("jvm") version "1.7.20"
}

group = "io.kotest.extensions"
version = Ci.version

dependencies {
   implementation(libs.kotest.framework.api)
   implementation(libs.kotest.framework.engine)
   testImplementation(libs.kotest.assertions.core)
   testImplementation(libs.kotest.runner.junit5)
}

tasks.named<Test>("test") {
   useJUnitPlatform()
   testLogging {
      showExceptions = true
      showStandardStreams = true
      exceptionFormat = TestExceptionFormat.FULL
   }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
   kotlinOptions.jvmTarget = "11"
}

repositories {
   mavenLocal()
   mavenCentral()
   maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
   }
}
