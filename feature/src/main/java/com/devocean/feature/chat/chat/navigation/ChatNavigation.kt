package com.devocean.feature.chat.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.devocean.core.navigation.MainTabRoute
import com.devocean.feature.chat.chat.ChatRoute
import com.devocean.feature.chat.selectedchat.navigation.navigateSelectedChat
import kotlinx.serialization.Serializable

fun NavController.navigateChat(navOptions: NavOptions? = null) {
    navigate(
        route = Chat,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.chatNavGraph(
    navHostController: NavHostController,
) {
    composable<Chat> {
        ChatRoute(
            navigateToSelectedChat = { navHostController.navigateSelectedChat(id = it) }
        )
    }
}

@Serializable
data object Chat : MainTabRoute