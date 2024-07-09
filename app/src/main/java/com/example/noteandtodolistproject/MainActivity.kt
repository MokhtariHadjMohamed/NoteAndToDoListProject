package com.example.noteandtodolistproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.noteandtodolistproject.ui.theme.NoteAndToDoListProjectTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDataBase::class.java,
            "noteAndToDoList.db"
        ).build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAndToDoListProjectTheme {
                val viewModel = viewModel<NoteViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return NoteViewModel(db.doa) as T
                        }
                    }
                )
                val shouldShowDialog = remember { mutableStateOf(false) }
                val allNote: State<List<Note>> = viewModel.noteList.observeAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(onClick = {
                        shouldShowDialog.value = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }) { innerPadding ->
                    if (shouldShowDialog.value) {
                        AddNoteDialog(shouldShowDialog = shouldShowDialog, viewModel)
                    } else {
                        NoteList(allNote.value, modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun NoteList(notes: List<Note>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LazyColumn {
        items(notes) { note ->
            ClickableText(
                text = AnnotatedString(note.title),
                onClick = {
                    Toast.makeText(context, note.id.toString(), Toast.LENGTH_LONG).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                style = TextStyle(
                    fontSize = 30.sp
                )
            )
        }
    }
}