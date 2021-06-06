package com.arvind.notewakeup.storage.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arvind.notewakeup.model.NoteModel


@Dao
interface NoteDao {

    // insert new note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertnote(noteModel: NoteModel)

    // update new note
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun udatatenote(noteModel: NoteModel)

    // delete new note
    @Delete
    suspend fun deletenote(noteModel: NoteModel)

    // get all saved note list
    @Query("SELECT * FROM notewakeup ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<NoteModel>>

    @Query("SELECT * FROM notewakeup WHERE noteTitle LIKE :query OR noteBody LIKE:query")
    fun getsearchNote(query: String?): LiveData<List<NoteModel>>

}