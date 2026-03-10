package ru.kazan.itis.bikmukhametov.network.cookie.impl

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.kazan.itis.bikmukhametov.network.cookie.api.CookiePersistence

/*
 * Хранит куки в памяти (основной источник) + персистит в DataStore для восстановления
 * после перезапуска приложения.
 *
 * Ktor вызывает addCookie() из receivePipeline (после ответа сервера) и
 * get() из sendPipeline (перед отправкой запроса). In-memory кэш гарантирует,
 * что get() сразу видит куки, сохранённые в addCookie() — без ожидания DataStore.
 */
internal class PersistentCookieStorage(
    private val persistence: CookiePersistence
) : CookiesStorage {

    private val mutex = Mutex()

    // Основное хранилище — память. Ключ — имя куки.
    private val memCache = mutableMapOf<String, Cookie>()

    // Загружаем из DataStore один раз при первом обращении.
    private var loaded = false

    /** Кэш строки Cookie для defaultRequest (не suspend). Обновляется в ensureLoaded/addCookie/clear. */
    private var cachedCookieHeader: String? = null

    private suspend fun ensureLoaded() {
        if (loaded) return
        val header = persistence.getCookieHeader()
        if (!header.isNullOrBlank()) {
            parseCookieHeader(header).forEach { memCache[it.name] = it }
        }
        loaded = true
        cachedCookieHeader = serializeCookies(memCache.values).takeIf { memCache.isNotEmpty() }
        println("COOKIE_STORAGE loaded from DataStore: keys=${memCache.keys}")
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        ensureLoaded()
        val cookies = memCache.values.filter { isCookieValid(it) }
        // Куки с auth.smartbotpro.ru должны уходить на metac-92.smartbotpro.ru — задаём домен верхнего уровня.
        val domain = rootDomain(requestUrl.host)
        val withDomain = cookies.map { it.copy(domain = domain) }
        println("COOKIE_STORAGE get(${requestUrl.host}), domain=$domain: returning ${withDomain.map { it.name }}")
        withDomain
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) = mutex.withLock {
        ensureLoaded()
        val domain = rootDomain(requestUrl.host)
        val normalized = cookie.copy(domain = domain)
        println("COOKIE_STORAGE addCookie: name=${normalized.name}, domain=$domain")
        memCache[normalized.name] = normalized
        val serialized = serializeCookies(memCache.values)
        persistence.setCookieHeader(serialized)
        cachedCookieHeader = serialized
        println("COOKIE_STORAGE cache now: ${memCache.keys}")
    }

    /** Синхронно возвращает строку для заголовка Cookie (для defaultRequest). */
    fun getCookieHeaderForRequestSync(): String? = cachedCookieHeader

    /* Вызывать при логауте / 401 — очищает и память, и DataStore. */
    suspend fun clear() = mutex.withLock {
        memCache.clear()
        loaded = false
        cachedCookieHeader = null
        persistence.clear()
        println("COOKIE_STORAGE cleared")
    }

    override fun close() = Unit

    private fun parseCookieHeader(header: String): List<Cookie> {
        if (header.isBlank()) return emptyList()
        return header.split(SEPARATOR)
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

    private fun serializeCookies(cookies: Collection<Cookie>): String =
        cookies.joinToString(SEPARATOR) { "${it.name}=${it.value}" }

    private fun isCookieValid(cookie: Cookie): Boolean {
        val expires = cookie.expires ?: return true
        return expires.timestamp > System.currentTimeMillis()
    }

    /** Домен верхнего уровня (metac-92.smartbotpro.ru → smartbotpro.ru), чтобы куки шли на все поддомены. */
    private fun rootDomain(host: String): String {
        val parts = host.split('.')
        return if (parts.size >= 2) parts.takeLast(2).joinToString(".") else host
    }

    companion object {
        private const val SEPARATOR = "; "
    }
}
