import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   id("kotest-publishing-conventions")
   kotlin("jvm") version "1.6.21"
}

group = "io.kotest.extensions"
version = Ci.version

dependencies {
   testImplementation(platform(libs.kotest.bom))
   testImplementation(libs.kotest.framework.api)
   testImplementation(libs.kotest.framework.engine)
   testImplementation(libs.kotest.assertions.core)
   testImplementation(libs.kotest.runner.junit5)
   testImplementation(libs.kotest.property)
}

tasks.named<Test>("test") {
   useJUnitPlatform()
   testLogging {
      showExceptions = true
      showStandardStreams = true
      exceptionFormat = TestExceptionFormat.FULL
   }
}

tasks.withType<KotlinCompile>().configureEach {
   kotlinOptions {
      jvmTarget = "11"
      this.freeCompilerArgs += listOf(
         "-opt-in=kotlin.time.ExperimentalTime",
      )
   }
}

repositories {
   mavenLocal()
   mavenCentral()
   maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
   }
}
