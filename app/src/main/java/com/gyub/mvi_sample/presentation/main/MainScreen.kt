package com.gyub.mvi_sample.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.gyub.mvi_sample.R
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.presentation.main.state.MainEvent
import com.gyub.mvi_sample.presentation.main.state.MainUiState
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    users: LazyPagingItems<GithubUser>,
    lazyListState: LazyListState,
    sendEvent: (MainEvent) -> Unit,
) {
    val isRefreshing = users.loadState.refresh is LoadState.Loading

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { sendEvent(MainEvent.Refresh) }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.searchQuery,
                    onValueChange = {
                        sendEvent(MainEvent.ChangeSearchQuery(it))
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            Image(
                                modifier = Modifier.clickable { sendEvent(MainEvent.DeleteSearchQuery) },
                                painter = painterResource(R.drawable.ic_input_delete),
                                contentDescription = null,
                            )
                        }
                    }
                )

                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
                ) {
                    items(users.itemCount) { index ->
                        GithubUserCard(
                            user = users[index]!!,
                            onClick = { sendEvent(MainEvent.ShowUserDetail(it.id)) }
                        )
                    }

                    when (users.loadState.append) {
                        is LoadState.Loading -> item { CircularProgressIndicator() }
                        is LoadState.Error -> item { Text("Error") }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun GithubUserCard(
    user: GithubUser,
    onClick: (GithubUser) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable { onClick(user) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.login, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = user.id.toString(), fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}


@Preview
@Composable
private fun PrevMainScreen() {
    MainScreen(
        uiState = MainUiState(),
        users = flowOf(PagingData.from(listOf(GithubUser(id = 1, login = "test", avatarUrl = "test")))).collectAsLazyPagingItems(),
        lazyListState = LazyListState(),
        sendEvent = {},
    )
}