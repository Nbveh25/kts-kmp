package ru.kazan.itis.bikmukhametov.network.auth.logout

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// События логаута (401). UI подписывается на logoutEvents и выполняет навигацию на Login.
class LogoutEventBus {

    private val _channel = Channel<Unit>(Channel.RENDEZVOUS)

    val logoutEvents = _channel.receiveAsFlow()

    fun trigger() {
        _channel.trySend(Unit)
    }
}
