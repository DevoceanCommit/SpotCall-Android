package com.devocean.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.devocean.core.designsystem.theme.DevoceanSpotTheme
import com.devocean.core.designsystem.theme.SpotGray
import com.devocean.core.extension.toast
import com.devocean.feature.home.component.HomeLatestData
import com.devocean.feature.home.component.HomeProfile
import com.devocean.feature.home.component.HomeSensorData
import com.devocean.feature.home.component.HomeTopBar
import com.devocean.feature.home.model.LatestChatListModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White
        )
    }

    LaunchedEffect(true) {
        viewModel.getLatestChat()
        viewModel.getSensor()
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is HomeSideEffect.ShowToast -> context.toast(sideEffect.message)
                }
            }
    }

    HomeScreen(
        paddingValues = paddingValues,
        temperature = state.temperature,
        movement = state.movement,
        sound = state.sound,
        humidity = state.humidity,
        latestChat = state.latestChat
    )
}

@Composable
fun HomeScreen(
    temperature: Int,
    movement: String,
    sound: Int,
    humidity: Int,
    latestChat: PersistentList<LatestChatListModel>,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        HomeTopBar()
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(thickness = 2.dp, color = SpotGray)
        Spacer(modifier = Modifier.height(20.dp))
        HomeProfile(modifier = Modifier.padding(start = 10.dp))
        Spacer(modifier = Modifier.height(20.dp))
        HomeSensorData(
            temperature = temperature,
            movement = movement,
            sound = sound,
            humidity = humidity
        )
        Spacer(modifier = Modifier.height(10.dp))
        HomeLatestData(
            chatList = latestChat,
            modifier = Modifier.height(290.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DevoceanSpotTheme {
        HomeScreen(
            temperature = 4,
            movement = "움직임",
            sound = 5,
            humidity = 6,
            latestChat = persistentListOf()
        )
    }
}