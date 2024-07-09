package com.example.noteandtodolistproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDOA
) : ViewModel() {

    var noteList: LiveData<List<Note>> = dao.getAllNotes().asLiveData()

    fun insertNote(note : Note){
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }
}