package ru.kazan.itis.bikmukhametov.network.cookie.impl

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.kazan.itis.bikmukhametov.network.cookie.api.CookiePersistence

/*
 * Обрабатывает Set-Cookie от бека, сохраняет в сторадж и подставляет куку в запросы.
 * при 401 нужно очищать сторадж и делать логаут.
 */
internal class PersistentCookieStorage(
    private val persistence: CookiePersistence
) : CookiesStorage {

    private val mutex = Mutex()

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        val header = persistence.getCookieHeader() ?: return emptyList()
        parseCookieHeader(header)
            .filter { isCookieValidForUrl(it) }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) = mutex.withLock {
        val header = persistence.getCookieHeader()
        val current = parseCookieHeader(header.orEmpty()).associateBy { it.name }.toMutableMap()
        current[cookie.name] = cookie
        persistence.setCookieHeader(serializeCookies(current.values))
    }

    override fun close() {
        // заглушка(
    }

    /* Очистить куки (логаут / 401). */
    private suspend fun clear() = mutex.withLock {
        persistence.clear()
    }

    private fun parseCookieHeader(header: String): List<Cookie> {
        if (header.isBlank()) return emptyList()
        return header.split(COOKIE_SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { part ->
                val eq = part.indexOf('=')
                if (eq <= 0) return@mapNotNull null
                val name = part.take(eq).trim()
                val value = part.substring(eq + 1).trim()
                if (name.isEmpty()) return@mapNotNull null
                Cookie(name = name, value = value)
            }
    }

    private fun isCookieValidForUrl(cookie: Cookie): Boolean {
        // При протухании бекенд вернёт 401 — делаем логаут; здесь только базовая проверка
        val expires = cookie.expires ?: return true
        return expires.timestamp > System.currentTimeMillis()
    }

    private fun serializeCookies(cookies: Collection<Cookie>): String =
        cookies.joinToString(COOKIE_SEPARATOR) { "${it.name}=${it.value}" }

    companion object {
        private const val COOKIE_SEPARATOR = "; "
    }
}
