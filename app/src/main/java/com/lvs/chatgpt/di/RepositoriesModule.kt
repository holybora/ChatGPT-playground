package com.lvs.chatgpt.di

import com.lvs.data.remote.api.OpenAIApi
import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.ConversationRepositoryImpl
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.MessageRepositoryImpl
import com.lvs.data.remote.repositories.OpenAIRepository
import com.lvs.data.remote.repositories.OpenAIRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object RepositoriesModule {

    @Provides
    fun provideMessagesRepository(messagesDao: MessagesDao): MessageRepository = MessageRepositoryImpl(messagesDao)

    @Provides
    fun provideConversationsRepository(conversationsDao: ConversationsDao): ConversationRepository =
        ConversationRepositoryImpl(conversationsDao)

    @Provides
    fun provideOpenAiRepository(openAIApi: OpenAIApi): OpenAIRepository = OpenAIRepositoryImpl(openAIApi)

}