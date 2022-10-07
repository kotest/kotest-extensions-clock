package io.kotest.extensions.clock

import java.time.Instant
import kotlin.time.Duration

/* Helper extension functions */

/** Create a new instant, incremented by [duration]. */
internal operator fun Instant.plus(duration: Duration): Instant =
   plusMillis(duration.inWholeMilliseconds)


/** Create a new instant, decremented by [duration]. */
internal operator fun Instant.minus(duration: Duration): Instant =
   minusMillis(duration.inWholeMilliseconds)
