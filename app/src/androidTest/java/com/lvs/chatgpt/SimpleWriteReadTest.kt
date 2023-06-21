package com.lvs.chatgpt

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lvs.data.remote.db.CGPTDatabase
import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SimpleEntityReadWriteTest {
    private lateinit var messagesDao: MessagesDao
    private lateinit var db: CGPTDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CGPTDatabase::class.java
        ).build()
        messagesDao = db.getMessagesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMessageAndCheckProperties() = runTest {
        val conversationId = 1L
        val textMessage = "message #1"
        val role = "system"

        val messageId = messagesDao.insert(MessageEntity.InsertionPrototype(conversationId, textMessage, role))

        val messages = messagesDao.getAllByConversationId(conversationId).firstOrNull()

        advanceUntilIdle()

        assert(messages != null) { "Messages shouldn't be a null" }

        assert(messages?.uid == messageId) {
            "Message id unexpected. Should be $messageId but was: ${messages?.uid}"
        }
        assert(messages?.text == textMessage) {
            "Message text unexpected. Should be $textMessage but was: ${messages?.text}"
        }
        assert(messages?.role == role) {
            "Message role unexpected. Should be $role but was: ${messages?.role}"
        }

    }
}