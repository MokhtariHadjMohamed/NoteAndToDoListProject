package com.example.noteandtodolistproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    var checked: Boolean
)
