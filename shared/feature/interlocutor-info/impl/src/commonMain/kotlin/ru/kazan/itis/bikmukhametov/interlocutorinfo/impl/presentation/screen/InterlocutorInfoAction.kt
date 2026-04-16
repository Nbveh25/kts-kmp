package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.screen

internal sealed interface InterlocutorInfoAction {

    data object RefreshUserVars : InterlocutorInfoAction

    data object RefreshUserLists : InterlocutorInfoAction

    data object RefreshUserChats : InterlocutorInfoAction

    data object RefreshUserPlannedEvents : InterlocutorInfoAction
}
