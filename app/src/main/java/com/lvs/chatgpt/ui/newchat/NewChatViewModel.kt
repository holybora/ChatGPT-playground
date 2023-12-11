package com.lvs.chatgpt.ui.newchat

import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.base.UiEvent
import com.lvs.chatgpt.base.UiState
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.InsertMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val createConversation: CreateConversationUseCase,
    private val insertMessageUseCase: InsertMessageUseCase
) : BaseViewModel<NewChatEvent, NewChatUiState>() {

    override val tag: String
        get() = NewChatViewModel::class.java.name

    override fun createInitialState(): NewChatUiState = NewChatUiState()

    override fun handleEvent(event: NewChatEvent) {
        when (event) {
            is NewChatEvent.OnSendMessage -> {
                launchOnBackground {
                    //TODO: error handling
                    val conversation = createConversation(event.text)
                    insertMessageUseCase(
                        InsertMessageUseCase.Params(
                            textToSend = event.text,
                            conversationId = conversation.id
                        )
                    )
                    setState {
                        copy(conversationId = conversation.id)
                    }
                }
                //create conversation
                //open chat screen
            }
        }
    }
}

data class NewChatUiState(val conversationId: Long? = null) : UiState {

}

sealed interface NewChatEvent : UiEvent {
    class OnSendMessage(val text: String) : NewChatEvent

}