package ru.kazan.itis.bikmukhametov.network.auth.model

data class AuthInfoModel(
    val manager: ManagerModel,
    val tokens: TokensModel
)

data class ManagerModel(
    val id: String,
    val email: String,
    val name: String,
    val confirmed: Boolean,
    val onboarding: OnboardingModel,
    val notificationChannelId: String?,
    val tgInfo: TgInfoModel?
)

data class OnboardingModel(
    val project: Boolean,
    val scenario: Boolean,
    val eventsAndActions: Boolean,
    val noChannelsPublish: Boolean,
    val noChannelsModal: Boolean,
    val linkedChannel: Boolean,
    val channelsPublish: Boolean,
    val prodVersion: Boolean,
    val resetChanges: Boolean,
    val scenarioSettings: Boolean,
    val graph: Boolean
)

data class TgInfoModel(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val link: String
)

data class TokensModel(
    val access: String? = null,
    val refresh: String? = null
)
