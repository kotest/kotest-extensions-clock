package io.kotest.extensions.clock

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration

class TestClock(
   private var instant: Instant,
   private val zone: ZoneId,
) : Clock() {

   override fun instant(): Instant = instant

   override fun withZone(zone: ZoneId): Clock {
      return TestClock(instant, zone)
   }

   override fun getZone(): ZoneId = zone

   /**
    * Sets the instant in this test clock to the given value.
    */
   fun setInstant(instant: Instant) {
      this.instant = instant
   }

   /**
    * Adds the given [duration] from the instant in this test clock.
    */
   operator fun plus(duration: Duration) {
      this.instant = instant.plusMillis(duration.inWholeMilliseconds)
   }

   /**
    * Removes the given [duration] from the instant in this test clock.
    */
   operator fun minus(duration: Duration) {
      this.instant = instant.minusMillis(duration.inWholeMilliseconds)
   }
}