package com.lvs.data.remote.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String,
){
    data class InsertionPrototype(val title: String)

    companion object {
        const val DEFAULT_CONVERSATION_ID = -1L
    }
}