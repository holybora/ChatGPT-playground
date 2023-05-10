package com.lvs.data.remote.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "conversation_id")
    val conversationId: String,
    val question: String,
    val answer: String,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String
) {
    data class InsertionPrototype(
        @ColumnInfo(name = "conversation_id")
        val conversationId: String,
        val question: String,
        val answer: String,
    )
}