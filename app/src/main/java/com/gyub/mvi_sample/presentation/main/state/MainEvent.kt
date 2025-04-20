package com.gyub.mvi_sample.presentation.main.state

import com.gyub.mvi_sample.presentation.base.Reducer

/**
 * 메인 화면 Evnet(Intent)
 */
sealed interface MainEvent : Reducer.Event {
    data object Refresh : MainEvent

    data class ChangeSearchQuery(val newQuery: String) : MainEvent

    data class ShowUserDetail(val userId: Int) : MainEvent

    data object DeleteSearchQuery : MainEvent
}