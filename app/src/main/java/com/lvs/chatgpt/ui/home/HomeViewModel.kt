package com.lvs.chatgpt.ui.home

import android.util.Log
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.base.UiEffect
import com.lvs.chatgpt.base.UiEvent
import com.lvs.chatgpt.base.UiState
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.domain.DeleteConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetSelectedConversationFlowUseCase
import com.lvs.domain.SetSelectedConversationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSelectedConversationFlow: GetSelectedConversationFlowUseCase,
    private val setSelectedConversation: SetSelectedConversationUseCase,
    private val getConversationsFlow: GetConversationsFlowUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
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
        launchOnBackground {
            setSelectedConversation(getConversationsFlow().first().firstOrNull()?.id ?: DEFAULT_CONVERSATION_ID)
        }
    }

    override val tag: String
        get() = HomeViewModel::class.java.name

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

            is HomeEvent.OnDeleteChatClicked -> {

                launchOnBackground {

                    val conversations = uiState.value.conversations
                    val index = uiState.value.conversations.indexOfFirst { it.id == event.conversationId }
                    val nextInTurnConversationId = when {
                        //deleted single conv, set to default
                        conversations.isEmpty() || conversations.size == 1 -> {
                            DEFAULT_CONVERSATION_ID
                        }
                        //deleted last, fetch previous
                        conversations.lastIndex == index -> {
                            conversations.lastIndex - 1
                        }
                        //fetch next in turn
                        else -> index + 1
                    }
                    //delete from db
                    runCatching { deleteConversationUseCase(event.conversationId) }
                        .onFailure {
                            when (it) {
                                is DeleteConversationUseCase.ConversationNonExistException -> {
                                    //TODO: show personal message to user about error
                                }
                            }
                        }
                        .getOrThrow()

                    Log.d(tag, "Next in turn id: $nextInTurnConversationId")
                    setSelectedConversation(conversations[nextInTurnConversationId.toInt()].id)

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
    class OnDeleteChatClicked(val conversationId: Long) : HomeEvent
}

sealed interface HomeEffect : UiEffect