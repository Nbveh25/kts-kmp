package ru.kazan.itis.bikmukhametov.network.auth.logout

import ru.kazan.itis.bikmukhametov.network.cookie.PersistentCookieStorage

internal class LogoutServiceImpl(
    private val cookieStorage: PersistentCookieStorage,
    private val logoutEventBus: LogoutEventBus
) : LogoutService {

    override suspend fun logout() {
        cookieStorage.clear()
        logoutEventBus.trigger()
    }
}
