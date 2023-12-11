package com.lvs.chatgpt.ui.stt

import android.util.Log
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.domain.GetFilePathFromUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TranscriptionViewModel @Inject constructor(val getFilePathFromUriUseCase: GetFilePathFromUriUseCase) :
    BaseViewModel<TranscriptionEvent, TranscriptionUiState>() {
    override val tag: String
        get() = TranscriptionViewModel::class.java.name

    override fun createInitialState(): TranscriptionUiState = TranscriptionUiState()

    override fun handleEvent(event: TranscriptionEvent) {
        when (event) {
            TranscriptionEvent.OnUploadClicked -> setState { copy(selectingVideo = true) }
            is TranscriptionEvent.OnVideoSelected -> {
                Log.d(tag, "selected video uri: ${event.uri}")
                launchOnBackground {
                    Log.d(tag, "selected video path: ${getFilePathFromUriUseCase(event.uri)}")
                }
                setState { copy(selectingVideo = false) }
            }

            TranscriptionEvent.OnSelectVideoShowed -> setState { copy(selectingVideo = false) }
        }
    }
}


