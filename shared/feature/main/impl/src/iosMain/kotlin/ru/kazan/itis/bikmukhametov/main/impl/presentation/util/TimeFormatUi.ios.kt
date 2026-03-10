package ru.kazan.itis.bikmukhametov.main.impl.presentation.util

import platform.Foundation.NSDate

private const val MILLIS_PER_SECOND = 1000L

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * MILLIS_PER_SECOND).toLong()
