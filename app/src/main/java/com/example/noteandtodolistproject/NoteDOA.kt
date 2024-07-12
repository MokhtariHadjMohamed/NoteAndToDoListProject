package com.example.noteandtodolistproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDOA {
     // TODO Note
     @Insert
     suspend fun insertNote(note :Note)

     @Update
     suspend fun updateNote(note: Note)

     @Delete
     suspend fun deleteNote(note: Note)

     @Query("SELECT * FROM Note")
     fun getAllNotes() : Flow<List<Note>>

     // TODO ToDoList
     @Insert
     suspend fun insertToDo(toDo :ToDoList)

     @Update
     suspend fun updateToDo(toDo: ToDoList)

     @Delete
     suspend fun deleteToDo(toDo: ToDoList)

     @Query("SELECT * FROM ToDoList")
     fun getAllToDos() : Flow<List<ToDoList>>
}
