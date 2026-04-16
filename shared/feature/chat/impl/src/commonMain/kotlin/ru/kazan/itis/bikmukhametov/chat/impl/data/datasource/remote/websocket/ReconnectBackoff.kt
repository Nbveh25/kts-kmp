package ru.kazan.itis.bikmukhametov.chat.impl.data.datasource.remote.websocket

import kotlin.math.min
import kotlin.random.Random

@Suppress("MagicNumber")
internal class ReconnectBackoff(
    private val minDelayMs: Long,
    private val maxDelayMs: Long,
) {
    var attempt: Int = 0
        private set

    fun reset() {
        attempt = 0
    }

    fun nextDelayMs(): Long {
        attempt++
        val exp = min(10, attempt) // guard from overflow
        val base = minDelayMs * (1L shl exp)
        val capped = min(maxDelayMs, base)
        val jitter = (capped * 0.2).toLong().coerceAtLeast(1L)
        return (capped - jitter) + Random.nextLong(0, jitter + 1)
    }
}
