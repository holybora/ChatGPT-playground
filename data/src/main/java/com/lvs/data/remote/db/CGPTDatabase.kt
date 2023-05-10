package com.lvs.data.remote.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lvs.data.remote.db.converters.Converters
import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.MessageEntity


@Database(entities = [MessageEntity::class, ConversationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CGPTDatabase : RoomDatabase() {

    abstract fun messagesDao(): MessagesDao
    abstract fun conversationsDao(): ConversationsDao

}