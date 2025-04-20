package com.gyub.mvi_sample.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gyub.mvi_sample.presentation.detail.state.DetailEvent
import com.gyub.mvi_sample.presentation.detail.state.DetailUiState


@Composable
fun DetailScreen(
    uiState: DetailUiState,
    sendEvent: (DetailEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { sendEvent(DetailEvent.ShowAvatarPreview) },
                    model = uiState.uiModel.avatarUrl,
                    contentDescription = null,
                )

                Text(text = uiState.uiModel.userName, style = MaterialTheme.typography.titleLarge)

                Text(uiState.uiModel.bio, style = MaterialTheme.typography.bodyLarge)

                Text(uiState.uiModel.location, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}