package ru.kazan.itis.bikmukhametov.network.auth.session

import io.github.aakira.napier.Napier
import ru.kazan.itis.bikmukhametov.network.auth.datasource.AuthDataSource

internal class SessionCheckerImpl(
    private val authDataSource: AuthDataSource
) : SessionChecker {

    override suspend fun isSessionValid(): Boolean {
        val result = authDataSource.fetchAuthInfo()

        Napier.d("fetchAuthInfo result: ${if (result.isSuccess) "SUCCESS" 
        else "FAILURE - ${result.exceptionOrNull()?.message}"}")

        if (result.isSuccess) {
            result.getOrNull()?.let { authInfo ->
                Napier.d("User: ${authInfo.manager.name} (${authInfo.manager.email})")
            }
            return true
        }

        // Для 401 у нас уже срабатывает HttpResponseValidator:
        //   - чистит куки
        //   - триггерит LogoutEventBus
        // Здесь просто возвращаем false.
        return false
    }
}

