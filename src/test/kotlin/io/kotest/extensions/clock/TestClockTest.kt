package io.kotest.extensions.clock

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.ArbitraryBuilder
import io.kotest.property.arbitrary.instant
import io.kotest.property.checkAll
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TestClockTest : FunSpec({

   test("expect TestClock + Instant creates new incremented TestClock") {
      checkAll(
         Arb.instant(instantRange),
         Arb.duration(),
         Arb.zoneId(),
      ) { initialInstant, duration, zoneId ->
         val testClock = TestClock(initialInstant, zoneId)
         val result = testClock + duration

         withClue("original TestClock instance should be unmodified") {
            testClock.instant() shouldBe initialInstant
            testClock.zone shouldBe zoneId
         }
         withClue("result should be a new TestClock instance") {
            result shouldNotBeSameInstanceAs testClock
         }
         withClue("result.zoneId should match original") {
            result.zone shouldBe zoneId
         }
         withClue("result.instant() should be modified") {
            if (duration.isPositive()) {
               result.instant() shouldBeGreaterThanOrEqualTo initialInstant
            } else {
               result.instant() shouldBeLessThanOrEqualTo initialInstant
            }
            result.instant() shouldBe (initialInstant + duration)
         }
         withClue("result should have the same zoneId") {
            result.zone shouldBe zoneId
         }
      }
   }

   test("expect TestClock - Instant creates new incremented TestClock") {
      checkAll(
         Arb.instant(instantRange),
         Arb.duration(),
         Arb.zoneId(),
      ) { initialInstant, duration, zoneId ->
         val testClock = TestClock(initialInstant, zoneId)
         val result = testClock - duration

         withClue("original TestClock instance should be unmodified") {
            testClock.instant() shouldBe initialInstant
            testClock.zone shouldBe zoneId
         }
         withClue("result should be a new TestClock instance") {
            result shouldNotBeSameInstanceAs testClock
         }
         withClue("result.zoneId should match original") {
            result.zone shouldBe zoneId
         }
         withClue("result.instant() should be modified") {
            if (duration.isPositive()) {
               testClock.instant() shouldBeLessThanOrEqualTo initialInstant
            } else {
               testClock.instant() shouldBeGreaterThanOrEqualTo initialInstant
            }
            result.instant() shouldBe (initialInstant - duration)
         }
         withClue("result should have the same zoneId") {
            result.zone shouldBe zoneId
         }
      }
   }

   test("expect TestClock += Instant increases time") {
      checkAll(
         Arb.instant(instantRange),
         Arb.duration(),
         Arb.zoneId(),
      ) { initialInstant, duration, zoneId ->
         val testClock = TestClock(initialInstant, zoneId)
         testClock += duration

         withClue("zone should be unmodified") {
            testClock.zone shouldBe zoneId
         }
         withClue("testClock.instant() should be modified") {
            if (duration.isPositive()) {
               testClock.instant() shouldBeGreaterThanOrEqualTo initialInstant
            } else {
               testClock.instant() shouldBeLessThanOrEqualTo initialInstant
            }
            testClock.instant() shouldBe (initialInstant + duration)
         }
      }
   }

   test("expect TestClock -= Instant decreases time") {
      checkAll(
         Arb.instant(instantRange),
         Arb.duration(),
         Arb.zoneId(),
      ) { initialInstant, duration, zoneId ->
         val testClock = TestClock(initialInstant, zoneId)
         testClock -= duration

         withClue("zone should be unmodified") {
            testClock.zone shouldBe zoneId
         }
         withClue("testClock.instant() should be modified") {
            if (duration.isPositive()) {
               testClock.instant() shouldBeLessThanOrEqualTo initialInstant
            } else {
               testClock.instant() shouldBeGreaterThanOrEqualTo initialInstant
            }
            testClock.instant() shouldBe (initialInstant - duration)
         }
      }
   }
}) {
   companion object {

      /**
       * Limit the generated range of Instants between years 1980 to 2100, otherwise `Arb.instant()`
       * generates Instants that are at the limit, and we can't plus/minus durations that go over
       * the limit.
       */
      private val instantRange: ClosedRange<Instant> =
         Instant.ofEpochSecond(315532800)..Instant.ofEpochSecond(4102444800)

      private fun Arb.Companion.duration(): Arb<Duration> =
         ArbitraryBuilder.create {
            val sign = if (it.random.nextBoolean()) 1 else -1
            it.random.nextInt().milliseconds * sign
         }.withShrinker {
            it.toComponents { days, hours, minutes, seconds, nanoseconds ->
               listOf(
                  days.days,
                  hours.hours,
                  minutes.minutes,
                  seconds.seconds,
                  nanoseconds.nanoseconds,
               ).distinct()
            }
         }.withEdgecases(
            buildList {
               add(Duration.ZERO)
               addAll(
                  DurationUnit.values().map { unit ->
                     1.toDuration(unit)
                  }
               )
            }.distinct()
         ).build()

      private fun Arb.Companion.zoneId(): Arb<ZoneId> =
         ArbitraryBuilder.create { rs ->
            val zoneId = ZoneId.getAvailableZoneIds().random(rs.random)
            ZoneId.of(zoneId)
         }.build()
   }
}
