package com.lvs.chatgpt.ui.home

import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.base.UiEffect
import com.lvs.chatgpt.base.UiEvent
import com.lvs.chatgpt.base.UiState
import com.lvs.data.remote.db.entities.ConversationEntity
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
            setSelectedConversation(getConversationsFlow().first().firstOrNull())
        }
    }

    override val tag: String
        get() = HomeViewModel::class.java.name

    override fun createInitialState(): HomeUiState = HomeUiState()

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnChatClicked -> {
                if (event.conversation?.id == currentState.selectedConversation?.id) return
                launchOnBackground {
                    setSelectedConversation(event.conversation)
                }
            }

            HomeEvent.OnNewChatClicked -> {
                launchOnBackground {
                    setSelectedConversation(null)
                }
            }

            is HomeEvent.OnDeleteChatClicked -> {
                launchOnBackground {
                    val conversation = event.conversation
                    if (conversation == null) return@launchOnBackground
                    else {
                        //delete from db
                        runCatching { deleteConversationUseCase(conversation.id) }
                            .onFailure {
                                when (it) {
                                    is DeleteConversationUseCase.ConversationNonExistException -> {
                                        //TODO: show personal message to user about error
                                    }
                                }
                            }
                            .getOrThrow()

                        val conversations = uiState.value.conversations
                        val index = uiState.value.conversations.indexOfFirst { it.id == event.conversation.id }
                        when {
                            //deleted single conv, set to default
                            conversations.isEmpty() || conversations.size == 1 -> {
                                setSelectedConversation(null)
                            }
                            //deleted last, fetch previous
                            conversations.lastIndex == index -> {
                                setSelectedConversation(conversations[conversations.lastIndex - 1])
                            }
                            //fetch next in turn
                            else -> setSelectedConversation(conversations[index + 1])
                        }

                    }
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
    class OnChatClicked(val conversation: ConversationEntity?) : HomeEvent
    object OnNewChatClicked : HomeEvent
    class OnDeleteChatClicked(val conversation: ConversationEntity?) : HomeEvent
}

sealed interface HomeEffect : UiEffect