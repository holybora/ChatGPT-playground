package com.lvs.chatgpt.ui.stt.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lvs.chatgpt.ui.stt.TranscriptionScreen
import com.lvs.chatgpt.ui.stt.TranscriptionViewModel

internal const val transcriptionRoute = "transcription"

fun NavController.navigateToTranscription(navOptions: NavOptions? = null) =
    navigate(transcriptionRoute) {
        launchSingleTop = true
    }

fun NavGraphBuilder.transcriptionScreen(openDrawer: () -> Unit) =
    composable(transcriptionRoute) {
        val viewModel = hiltViewModel<TranscriptionViewModel>()
        TranscriptionScreen(
            viewModel = viewModel,
            onDrawerClick = openDrawer
        )
    }