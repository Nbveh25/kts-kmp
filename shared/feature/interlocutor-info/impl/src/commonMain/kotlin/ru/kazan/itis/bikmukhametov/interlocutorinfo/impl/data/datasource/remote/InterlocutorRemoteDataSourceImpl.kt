package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.data.datasource.remote

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.datasource.InterlocutorDataSource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorCustomField
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorPlannedEvent
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserChat
import ru.kazan.itis.bikmukhametov.interlocutorinfo.api.model.InterlocutorUserList
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.BuildKonfig
import ru.kazan.itis.bikmukhametov.network.error.mapApiError
import ru.kazan.itis.bikmukhametov.network.error.runCatchingCancelable

internal class InterlocutorRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : InterlocutorDataSource {

    override suspend fun getUserVars(chatId: String, userId: String): Result<List<InterlocutorCustomField>> {
        val raw = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/users/get_user_vars",
            ) {
                url {
                    parameters.append("chat_id", chatId)
                    parameters.append("user_id", userId)
                }
            }
            val body = response.body<GetUserVarsApiResponse>()
            if (body.status != "ok") {
                error("get_user_vars: status=${body.status}")
            }
            body.data?.vars.orEmpty().map { it.toModel() }
        }

        Napier.d(tag = TAG) {
            "getUserVars chatId=$chatId userId=$userId success=${raw.isSuccess} count=${raw.getOrNull()?.size}"
        }

        return raw.mapApiError("Не удалось загрузить переменные")
    }

    override suspend fun getUserLists(userId: String): Result<List<InterlocutorUserList>> {
        val raw = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/users/get_user_lists",
            ) {
                url {
                    parameters.append("user_id", userId)
                }
            }
            val body = response.body<GetUserListsApiResponse>()
            if (body.status != "ok") {
                error("get_user_lists: status=${body.status}")
            }
            body.data?.lists.orEmpty().mapNotNull { it.toModel() }
        }

        Napier.d(tag = TAG) {
            "getUserLists userId=$userId success=${raw.isSuccess} count=${raw.getOrNull()?.size}"
        }

        return raw.mapApiError("Не удалось загрузить списки")
    }

    override suspend fun getUserChats(userId: String): Result<List<InterlocutorUserChat>> {
        val raw = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/users/get_user_chats",
            ) {
                url {
                    parameters.append("user_id", userId)
                }
            }
            val body = response.body<GetUserChatsApiResponse>()
            if (body.status != "ok") {
                error("get_user_chats: status=${body.status}")
            }
            body.data?.chats.orEmpty().mapNotNull { it.toModel() }
        }

        Napier.d(tag = TAG) {
            "getUserChats userId=$userId success=${raw.isSuccess} count=${raw.getOrNull()?.size}"
        }

        return raw.mapApiError("Не удалось загрузить чаты")
    }

    override suspend fun getUserPlannedEvents(chatId: String, userId: String): Result<List<InterlocutorPlannedEvent>> {
        val raw = runCatchingCancelable {
            val response = httpClient.get(
                urlString = BuildKonfig.BASE_URL + "/api/users/get_user_planned_events",
            ) {
                url {
                    parameters.append("chat_id", chatId)
                    parameters.append("user_id", userId)
                }
            }
            val body = response.body<GetUserPlannedEventsApiResponse>()
            if (body.status != "ok") {
                error("get_user_planned_events: status=${body.status}")
            }
            body.data?.events.orEmpty().mapNotNull { it.toModel() }
        }

        Napier.d(tag = TAG) {
            "getUserPlannedEvents chatId=$chatId userId=$userId success=${raw.isSuccess} count=${raw.getOrNull()?.size}"
        }

        return raw.mapApiError("Не удалось загрузить отложенные события")
    }

    private companion object {
        const val TAG = "InterlocutorUserApi"
    }
}
