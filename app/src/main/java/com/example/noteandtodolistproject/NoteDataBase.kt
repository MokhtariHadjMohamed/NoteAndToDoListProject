package com.example.noteandtodolistproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDataBase: RoomDatabase() {

    abstract val doa: NoteDOA
}