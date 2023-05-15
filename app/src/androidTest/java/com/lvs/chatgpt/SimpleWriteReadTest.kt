package com.lvs.chatgpt

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lvs.data.remote.db.CGPTDatabase
import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
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
        val isBot = false

        val messageId = messagesDao.insert(MessageEntity.InsertionPrototype(conversationId, textMessage, isBot))

        val messages = messagesDao.getAllByConversationId(conversationId).firstOrNull()

        advanceUntilIdle()

        assert(messages != null) { "Messages shouldn't be a null" }

        val messageInTable = messages?.first()

        assert(messageInTable?.uid == messageId) {
            "Message id unexpected. Should be $messageId but list is: ${messageInTable?.uid}"
        }
        assert(messageInTable?.text == textMessage) {
            "Message text unexpected. Should be $textMessage but list is: ${messageInTable?.text}"
        }
        assert(messageInTable?.isBot == isBot) {
            "Message isBot unexpected. Should be $isBot but list is: ${messageInTable?.isBot}"
        }

    }
}