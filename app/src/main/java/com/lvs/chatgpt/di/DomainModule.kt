package com.lvs.chatgpt.di

import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreateConversationUseCase(conversationRepository: ConversationRepository) =
        CreateConversationUseCase(conversationRepository)

    @Provides
    fun provideGetConversationUseCase(conversationRepository: ConversationRepository) =
        GetConversationUseCase(conversationRepository)

    @Provides
    fun provideSendMessageToGPTUseCase(messagesRepository: MessageRepository, openAIRepository: OpenAIRepository) =
        SendMessageToChatGPTUseCase(messagesRepository, openAIRepository)

    @Provides
    fun provideGetMessagesByConversationIdUseCase(messagesRepository: MessageRepository,) =
        GetMessagesByConversationIdUseCase(messagesRepository)

}