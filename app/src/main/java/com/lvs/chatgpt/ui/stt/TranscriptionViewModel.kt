package com.lvs.chatgpt.ui.stt

import android.util.Log
import androidx.core.net.toUri
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.domain.GetAudioFromVideoUseCase
import com.lvs.domain.GetFilePathFromUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TranscriptionViewModel @Inject constructor(
    val getFilePathFromUriUseCase: GetFilePathFromUriUseCase,
    val getAudioFromVideoUseCase: GetAudioFromVideoUseCase
) :
    BaseViewModel<TranscriptionEvent, TranscriptionUiState>() {
    override val tag: String
        get() = TranscriptionViewModel::class.java.name

    override fun createInitialState(): TranscriptionUiState = TranscriptionUiState()

    override fun handleEvent(event: TranscriptionEvent) {
        when (event) {
            TranscriptionEvent.OnUploadClicked ->
                setState { copy(selectingVideo = true) }

            is TranscriptionEvent.OnVideoSelected -> {
                Log.d(tag, "selected video uri: ${event.uri}")
                launchOnBackground {
                    val videoPath = getFilePathFromUriUseCase(event.uri)
                    Log.d(tag, "selected video path: ${getFilePathFromUriUseCase(event.uri)}")
                    getAudioFromVideoUseCase(File(videoPath).toUri())
                }
                setState { copy(selectingVideo = false) }
            }

            TranscriptionEvent.OnSelectVideoShowed -> setState { copy(selectingVideo = false) }
        }
    }
}


