package ru.kazan.itis.bikmukhametov.main.impl.presentation.util

import platform.Foundation.NSDate

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
