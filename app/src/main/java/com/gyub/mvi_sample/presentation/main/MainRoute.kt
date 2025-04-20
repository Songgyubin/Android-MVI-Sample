package com.gyub.mvi_sample.presentation.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.gyub.mvi_sample.presentation.main.state.MainSideEffect
import kotlinx.coroutines.flow.Flow

/**
 * 메인 화면 Route
 */
@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    navigateUserDetail: (id: Int) -> Unit,
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val users = viewModel.users.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainScreen(
            uiState = uiState,
            users = users,
            lazyListState = listState,
            sendEvent = viewModel::sendEvent
        )
    }

    MainSideEffectsHandler(
        sideEffectChannel = viewModel.sideEffectChannel,
        refresh = { users.refresh() },
        saveSearchQuery = { query -> viewModel.saveSearchQuery(query) },
        navigateUserDetail = navigateUserDetail,
        showToast = { Toast.makeText(context, context.getString(it), Toast.LENGTH_LONG).show() }
    )
}

@Composable
private fun MainSideEffectsHandler(
    sideEffectChannel: Flow<MainSideEffect>,
    showToast: (messageId: Int) -> Unit,
    saveSearchQuery: (query: String) -> Unit,
    refresh: () -> Unit,
    navigateUserDetail: (id: Int) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffectChannel.collect { sideEffect ->
            when (sideEffect) {
                is MainSideEffect.NavigateUserDetail -> {
                    navigateUserDetail(sideEffect.userId)
                }

                MainSideEffect.Refresh -> {
                    refresh()
                }

                is MainSideEffect.ShowToastMessage -> {
                    showToast(sideEffect.messageId)
                }

                is MainSideEffect.ChangeSearchQuery -> {
                    saveSearchQuery(sideEffect.query)
                }
            }
        }
    }
}