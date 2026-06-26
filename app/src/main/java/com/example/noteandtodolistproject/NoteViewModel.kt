package com.example.noteandtodolistproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val dao: NoteDOA
) : ViewModel() {

    var noteList: LiveData<List<Note>> = dao.getAllNotes().asLiveData()
    var toDoList: LiveData<List<ToDoList>> = dao.getAllToDos().asLiveData()

    fun insertNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            dao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }

    fun insertToDoList(toDoList: ToDoList) {
        viewModelScope.launch {
            dao.insertToDo(toDoList)
        }
    }

    fun updateToDoList(toDoList: ToDoList) {
        viewModelScope.launch {
            dao.updateToDo(toDoList)
        }
    }

    fun deleteToDoList(toDoList: ToDoList) {
        viewModelScope.launch {
            dao.deleteToDo(toDoList)
        }
    }
}