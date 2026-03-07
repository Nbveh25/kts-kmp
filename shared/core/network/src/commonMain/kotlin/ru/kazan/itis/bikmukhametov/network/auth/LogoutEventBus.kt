package ru.kazan.itis.bikmukhametov.network.auth

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * События логаута (401). UI подписывается на [logoutEvents] и выполняет навигацию на Login.
 */
class LogoutEventBus {

    private val _channel = Channel<Unit>(Channel.RENDEZVOUS)

    /** Flow событий логаута. Коллектить в UI для навигации на экран логина. */
    val logoutEvents = _channel.receiveAsFlow()

    /** Вызвать при 401 — уведомляет подписчиков. Неблокирующий. */
    fun trigger() {
        _channel.trySend(Unit)
    }
}
