package com.example.noteandtodolistproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteandtodolistproject.ui.theme.NoteAndToDoListProjectTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Example: Create a new table if needed
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS ToDoList (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                text TEXT NOT NULL,
                checked INTEGER NOT NULL
                )
        """.trimIndent()
            )
        }
    }

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext, NoteDataBase::class.java, "noteAndToDoList.db"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAndToDoListProjectTheme {
                val viewModel =
                    viewModel<NoteViewModel>(factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return NoteViewModel(db.doa) as T
                        }
                    })
                val itemsNavigation = listOf(
                    NavigationItem(
                        title = "Note",
                        selectedIcon = Icons.Filled.Add,
                        unSelectedIcon = Icons.Outlined.Add,
                    ),
                    NavigationItem(
                        title = "ToDoList",
                        selectedIcon = Icons.Filled.Done,
                        unSelectedIcon = Icons.Outlined.Done,
                        badgeCount = 45
                    ),
                )

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            itemsNavigation.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(text = item.title) },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        selectedItemIndex = index
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else {
                                                item.unSelectedIcon
                                            }, contentDescription = item.title
                                        )
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    }, drawerState = drawerState
                ) {
                    // TODO NoteCompose
                    if (selectedItemIndex == 0) {
                        val shouldShowDialogAdd = remember { mutableStateOf(false) }
                        val shouldShowDialogEdit = remember { mutableStateOf(false) }
                        val shouldShowDialogDelete = remember { mutableStateOf(false) }
                        val noteState = remember { mutableStateOf(Note(0, "", "")) }
                        val allNote: State<List<Note>> =
                            viewModel.noteList.observeAsState(initial = emptyList())
                        Scaffold(topBar = {
                            TopAppBar(colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ), title = {
                                Text("Notes")
                            }, navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu Button"
                                    )
                                }
                            })
                        }, modifier = Modifier.fillMaxSize(), floatingActionButton = {
                            FloatingActionButton(onClick = {
                                shouldShowDialogAdd.value = true
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }) { innerPadding ->
                            if (shouldShowDialogAdd.value) {
                                AddNoteDialog(shouldShowDialog = shouldShowDialogAdd, viewModel)
                            } else if (shouldShowDialogEdit.value) {
                                EditNoteDialog(
                                    shouldShowDialog = shouldShowDialogEdit,
                                    viewModel,
                                    noteState.value
                                )
                            } else if (shouldShowDialogDelete.value) {
                                DeleteNoteDialog(
                                    shouldShowDialog = shouldShowDialogDelete,
                                    viewModel,
                                    noteState.value
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.padding(innerPadding),
                                ) {
                                    items(allNote.value) { note ->
                                        Row {
                                            ClickableText(
                                                text = AnnotatedString(note.title),
                                                onClick = {
                                                    shouldShowDialogEdit.value = true
                                                    noteState.value = note
                                                    Toast.makeText(
                                                        applicationContext,
                                                        note.id.toString(),
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                                    .weight(1f),
                                                style = TextStyle(
                                                    fontSize = 25.sp
                                                )
                                            )
                                            IconButton(onClick = {
                                                noteState.value = note
                                                shouldShowDialogDelete.value = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete contact"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // TODO ToDoListCompose
                    else {
                        val shouldShowDialogAdd = remember { mutableStateOf(false) }
                        val shouldShowDialogEdit = remember { mutableStateOf(false) }
                        val shouldShowDialogDelete = remember { mutableStateOf(false) }
                        val toDoListState = remember { mutableStateOf(ToDoList(0, "", false)) }
                        val allToDoList: State<List<ToDoList>> =
                            viewModel.toDoList.observeAsState(initial = emptyList())
                        var checked by remember { mutableStateOf(false) }
                        Scaffold(topBar = {
                            TopAppBar(colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ), title = {
                                Text("To Do List")
                            }, navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu Button"
                                    )
                                }
                            })
                        }, modifier = Modifier.fillMaxSize(), floatingActionButton = {
                            FloatingActionButton(onClick = {
                                shouldShowDialogAdd.value = true
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }) { innerPadding ->
                            if (shouldShowDialogAdd.value) {
                                AddToDoListDialog(shouldShowDialog = shouldShowDialogAdd, viewModel)
                            } else if (shouldShowDialogEdit.value) {
                                EditToDoListDialog(
                                    shouldShowDialog = shouldShowDialogEdit,
                                    viewModel,
                                    toDoListState.value
                                )
                            } else if (shouldShowDialogDelete.value) {
                                DeleteToDoListDialog(
                                    shouldShowDialog = shouldShowDialogDelete,
                                    viewModel,
                                    toDoListState.value
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.padding(innerPadding),
                                ) {
                                    items(allToDoList.value) { toDoList ->
                                        checked = toDoList.checked
                                        Row {
                                            Checkbox(
                                                checked = checked,
                                                onCheckedChange = {
                                                    toDoList.checked = it
                                                    checked = toDoList.checked
                                                    viewModel.updateToDoList(toDoList)
                                                }
                                            )
                                            ClickableText(
                                                text = AnnotatedString(toDoList.text),
                                                onClick = {
                                                    shouldShowDialogEdit.value = true
                                                    toDoListState.value = toDoList
                                                    Toast.makeText(
                                                        applicationContext,
                                                        toDoList.id.toString(),
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                                    .weight(1f),
                                                style = TextStyle(
                                                    fontSize = 25.sp
                                                )
                                            )
                                            IconButton(onClick = {
                                                toDoListState.value = toDoList
                                                shouldShowDialogDelete.value = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete contact"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}