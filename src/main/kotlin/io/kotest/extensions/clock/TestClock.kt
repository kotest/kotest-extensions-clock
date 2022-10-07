package io.kotest.extensions.clock

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration

/**
 * A mutable [Clock] that supports millisecond precision.
 *
 * The clock is fixed until it is mutated using [plus], [plusAssign], [minus], [minusAssign].
 */
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
    * Sets the [instant] in this test clock to the given value.
    */
   fun setInstant(instant: Instant) {
      this.instant = instant
   }

   /**
    * Creates a new instance with the given [duration] added from the [instant].
    *
    * The current instance is unaffected.
    */
   operator fun plus(duration: Duration): TestClock =
      TestClock(
         instant = instant + duration,
         zone = zone,
      )

   /**
    * Adds the given [duration] from the instant in this test clock.
    */
   operator fun plusAssign(duration: Duration) {
      setInstant(instant + duration)
   }

   /**
    * Creates a new instance with the given [duration] removed from the [instant].
    *
    * The current instance is unaffected.
    */
   operator fun minus(duration: Duration): TestClock =
      TestClock(
         instant = instant - duration,
         zone = zone,
      )

   /**
    * Removes the given [duration] from the [instant] in this test clock.
    */
   operator fun minusAssign(duration: Duration) {
      setInstant(instant - duration)
   }
}
