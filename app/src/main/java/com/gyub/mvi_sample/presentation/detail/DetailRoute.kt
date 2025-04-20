package com.gyub.mvi_sample.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gyub.mvi_sample.presentation.detail.component.AvatarPreviewPopup
import com.gyub.mvi_sample.presentation.detail.state.DetailSideEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailRoute(
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isShowAvatarPreviewPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DetailScreen(
            uiState = uiState,
            sendEvent = viewModel::sendEvent
        )
    }

    if (isShowAvatarPreviewPopup) {
        AvatarPreviewPopup(
            imageUrl = uiState.uiModel.avatarUrl,
            onDismissRequest = { isShowAvatarPreviewPopup = false }
        )
    }

    DetailSideEffectsHandler(
        sideEffectChannel = viewModel.sideEffectChannel,
        loadUserDetail = viewModel::loadUserDetail,
        showAvatarPreviewPopup = { isShowAvatarPreviewPopup = true },
        showToast = { Toast.makeText(context, context.getString(it), Toast.LENGTH_LONG).show() }
    )
}

@Composable
private fun DetailSideEffectsHandler(
    sideEffectChannel: Flow<DetailSideEffect>,
    showToast: (messageId: Int) -> Unit,
    loadUserDetail: () -> Unit,
    showAvatarPreviewPopup: () -> Unit
) {
    LaunchedEffect(Unit) {
        sideEffectChannel.collect { sideEffect ->
            when (sideEffect) {
                is DetailSideEffect.ShowToastMessage -> {
                    showToast(sideEffect.messageId)
                }

                DetailSideEffect.Load -> {
                    loadUserDetail()
                }

                is DetailSideEffect.LoadedUserDetail -> {

                }

                DetailSideEffect.ShowAvatarPreviewPopup -> {
                    showAvatarPreviewPopup()
                }
            }
        }
    }
}