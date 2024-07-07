package com.example.noteandtodolistproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.noteandtodolistproject.ui.theme.NoteAndToDoListProjectTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAndToDoListProjectTheme {
                val shouldShowDialog = remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(onClick = {
                        shouldShowDialog.value = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }) { innerPadding ->
                    if (shouldShowDialog.value) {
                        AddNoteDialog(shouldShowDialog = shouldShowDialog)
                    }
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteAndToDoListProjectTheme {
        Greeting("Android")
    }
}