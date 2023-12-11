package com.lvs.chatgpt.di

import com.lvs.data.remote.api.OpenAIApi
import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.repositories.ConversationRepository
import com.lvs.data.repositories.ConversationRepositoryImpl
import com.lvs.data.repositories.MessageRepository
import com.lvs.data.repositories.MessageRepositoryImpl
import com.lvs.data.repositories.OpenAIRepository
import com.lvs.data.repositories.OpenAIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideMessagesRepository(messagesDao: MessagesDao): MessageRepository = MessageRepositoryImpl(messagesDao)

    @Provides
    @Singleton
    fun provideConversationsRepository(conversationsDao: ConversationsDao): ConversationRepository {
        return ConversationRepositoryImpl(conversationsDao)
    }

    @Provides
    @Singleton
    fun provideOpenAiRepository(openAIApi: OpenAIApi): OpenAIRepository = OpenAIRepositoryImpl(openAIApi)

}