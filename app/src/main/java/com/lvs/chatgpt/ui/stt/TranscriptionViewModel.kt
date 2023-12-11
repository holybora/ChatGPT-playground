package com.lvs.chatgpt.ui.stt

import com.lvs.chatgpt.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TranscriptionViewModel @Inject constructor() : BaseViewModel<TranscriptionEvent, TranscriptionUiState>() {
    override val tag: String
        get() = TranscriptionViewModel::class.java.name

    override fun createInitialState(): TranscriptionUiState = TranscriptionUiState()

    override fun handleEvent(event: TranscriptionEvent) {
        when (event) {

        }
    }
}


