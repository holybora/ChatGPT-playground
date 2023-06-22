package com.lvs.chatgpt.ui.home

import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.base.UiEffect
import com.lvs.chatgpt.base.UiEvent
import com.lvs.chatgpt.base.UiState
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetSelectedConversationFlowUseCase
import com.lvs.domain.SetSelectedConversationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSelectedConversationFlow: GetSelectedConversationFlowUseCase,
    private val setSelectedConversation: SetSelectedConversationUseCase,
    private val getConversationsFlow: GetConversationsFlowUseCase,
) : BaseViewModel<HomeEvent, HomeUiState, HomeEffect>() {

    init {
        launchOnBackground {
            getSelectedConversationFlow()
                .collect { setState { copy(selectedConversation = it) } }
        }
        launchOnBackground {
            getConversationsFlow()
                .collect { setState { copy(conversations = it) } }
        }
    }

    override fun createInitialState(): HomeUiState = HomeUiState()

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnChatClicked -> {
                if (event.chatId == currentState.selectedConversation?.id) return
                launchOnBackground {
                    setSelectedConversation(event.chatId)
                }
            }

            HomeEvent.OnNewChatClicked -> {
                launchOnBackground {
                    setSelectedConversation(DEFAULT_CONVERSATION_ID)
                }
            }
        }
    }

}

data class HomeUiState(
    val selectedConversation: ConversationEntity? = null,
    val conversations: List<ConversationEntity> = emptyList()
) : UiState

sealed interface HomeEvent : UiEvent {
    class OnChatClicked(val chatId: Long) : HomeEvent
    object OnNewChatClicked : HomeEvent
}

sealed interface HomeEffect : UiEffect