package com.palash.roomdatabase_noteapp.room_db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


//indices is use for unique record Name or Phone
@Entity(tableName = "tableName", indices = [Index(value = ["title"], unique = true)])
data class NoteDataClass(

    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val title: String,
    val content: String,
    val tags: String,
    val createdDate:String
)

