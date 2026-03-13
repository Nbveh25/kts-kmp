package ru.kazan.itis.bikmukhametov.network.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AuthInfoResponse(
    @SerialName("status") val status: String,
    @SerialName("data") val data: AuthData
)

@Serializable
internal data class AuthData(
    @SerialName("manager") val manager: Manager,
    @SerialName("tokens") val tokens: Tokens
)

@Serializable
internal data class Manager(
    @SerialName("_id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("confirmed") val confirmed: Boolean,
    @SerialName("extra") val extra: Extra,
    @SerialName("external_ids") val externalIds: ExternalIds
)

@Serializable
internal data class Extra(
    @SerialName("onboarding") val onboarding: Onboarding,
    @SerialName("notification_channel_id") val notificationChannelId: String?
)

@Serializable
internal data class Onboarding(
    @SerialName("project") val project: Boolean,
    @SerialName("scenario") val scenario: Boolean,
    @SerialName("events_and_actions") val eventsAndActions: Boolean,
    @SerialName("no_channels_publish") val noChannelsPublish: Boolean,
    @SerialName("no_channels_modal") val noChannelsModal: Boolean,
    @SerialName("linked_channel") val linkedChannel: Boolean,
    @SerialName("channels_publish") val channelsPublish: Boolean,
    @SerialName("prod_version") val prodVersion: Boolean,
    @SerialName("reset_changes") val resetChanges: Boolean,
    @SerialName("scenario_settings") val scenarioSettings: Boolean,
    @SerialName("graph") val graph: Boolean
)

@Serializable
internal data class ExternalIds(
    @SerialName("tg") val tg: TgInfo?
)

@Serializable
internal data class TgInfo(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("link") val link: String
)

@Serializable
internal data class Tokens(
    val access: String? = null,
    val refresh: String? = null
)