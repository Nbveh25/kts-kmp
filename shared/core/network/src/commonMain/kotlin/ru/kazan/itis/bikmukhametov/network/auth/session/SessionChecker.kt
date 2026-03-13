package ru.kazan.itis.bikmukhametov.network.auth.session


// Проверка актуальности сессии пользователя.
interface SessionChecker {

    /*
     * Возвращает true, если сервер подтверждает, что сессия ещё валидна.
     * Любые ошибки сети / 401 / неожиданные статусы трактуем как "сессия не валидна".
     */
    suspend fun isSessionValid(): Boolean
}

