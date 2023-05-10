package com.lvs.chatgpt.di

import android.app.Application
import androidx.room.Room
import com.lvs.data.remote.db.CGPTDatabase
import com.lvs.data.remote.db.dao.MessagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext application: Application): CGPTDatabase =
        Room.databaseBuilder(
            context = application,
            klass = CGPTDatabase::class.java,
            name = "cgpt_database"
        )
            .build()

    @Singleton
    @Provides
    fun provideMessagesDao(database: CGPTDatabase): MessagesDao = database.messagesDao()


}
