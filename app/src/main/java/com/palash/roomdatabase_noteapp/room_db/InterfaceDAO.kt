package com.palash.roomdatabase_noteapp.room_db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InterfaceDAO {

    //
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(mDataClass: NoteDataClass):Long

    @Update
    suspend fun updateData(mDataClass: NoteDataClass):Int

    @Delete
    suspend fun deleteData(mDataClass: NoteDataClass):Int

    @Query("DELETE FROM tableName WHERE id = :id")
    fun deleteDataById(id: Long):Int

    @Query("SELECT * FROM tableName")
    fun getAllRecord():LiveData<List<NoteDataClass>>

    // Query to retrieve a specific Note by its ID
    @Query("SELECT * FROM tableName WHERE id = :id")
    fun getRecordById(id: Long): NoteDataClass

  //  @Query("SELECT * FROM tableName WHERE title LIKE '%' || :title || '%'")
    @Query("SELECT * FROM tableName " +
            "WHERE title LIKE '%' || :query || '%' " +
            "OR content LIKE '%' || :query || '%' " +
            "OR tags LIKE '%' || :query || '%' " +
            "OR createdDate LIKE '%' || :query || '%'")
    fun searchRecordByTitle(query: String): LiveData<List<NoteDataClass>>
}