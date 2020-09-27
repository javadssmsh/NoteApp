package developer.company.mynotes.db.entities

import androidx.room.*
import developer.company.mynotes.db.entities.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes():List<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)
}