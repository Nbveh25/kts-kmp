package ru.kazan.itis.bikmukhametov.network.auth.mapper


import ru.kazan.itis.bikmukhametov.network.auth.model.AuthInfoModel
import ru.kazan.itis.bikmukhametov.network.auth.model.ManagerModel
import ru.kazan.itis.bikmukhametov.network.auth.model.OnboardingModel
import ru.kazan.itis.bikmukhametov.network.auth.model.TgInfoModel
import ru.kazan.itis.bikmukhametov.network.auth.model.TokensModel
import ru.kazan.itis.bikmukhametov.network.auth.response.AuthInfoResponse
import ru.kazan.itis.bikmukhametov.network.auth.response.Manager
import ru.kazan.itis.bikmukhametov.network.auth.response.Onboarding
import ru.kazan.itis.bikmukhametov.network.auth.response.TgInfo
import ru.kazan.itis.bikmukhametov.network.auth.response.Tokens

internal fun AuthInfoResponse.toModel(): AuthInfoModel = AuthInfoModel(
    manager = data.manager.toModel(),
    tokens = data.tokens.toModel()
)

internal fun Manager.toModel(): ManagerModel = ManagerModel(
    id = id,
    email = email,
    name = name,
    confirmed = confirmed,
    onboarding = extra.onboarding.toModel(),
    notificationChannelId = extra.notificationChannelId,
    tgInfo = externalIds.tg?.toModel()
)

internal fun Onboarding.toModel(): OnboardingModel = OnboardingModel(
    project = project,
    scenario = scenario,
    eventsAndActions = eventsAndActions,
    noChannelsPublish = noChannelsPublish,
    noChannelsModal = noChannelsModal,
    linkedChannel = linkedChannel,
    channelsPublish = channelsPublish,
    prodVersion = prodVersion,
    resetChanges = resetChanges,
    scenarioSettings = scenarioSettings,
    graph = graph
)

internal fun TgInfo.toModel(): TgInfoModel = TgInfoModel(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    link = link.trim()
)

internal fun Tokens.toModel(): TokensModel = TokensModel(
    access = access,
    refresh = refresh
)
