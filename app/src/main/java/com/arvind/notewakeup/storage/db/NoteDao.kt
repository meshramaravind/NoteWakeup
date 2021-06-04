package com.arvind.notewakeup.storage.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arvind.notewakeup.model.NoteModel

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertnote(noteModel: NoteModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun udatatenote(noteModel: NoteModel)

    @Delete
    suspend fun deletenote(noteModel: NoteModel)

    @Query("SELECT * FROM notewakeup ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<NoteModel>>


}