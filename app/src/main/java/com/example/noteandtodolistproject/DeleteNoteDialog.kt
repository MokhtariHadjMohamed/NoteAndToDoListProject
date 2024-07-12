package com.example.noteandtodolistproject

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.room.PrimaryKey

@Composable
fun DeleteNoteDialog(
    shouldShowDialog: MutableState<Boolean>,
    viewModel: NoteViewModel,
    noteSelected: Note
) {
    var context = LocalContext.current
    AlertDialog(
        onDismissRequest = { shouldShowDialog.value = false },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.deleteNote(noteSelected)
                    shouldShowDialog.value = false
                    Toast.makeText(context, noteSelected.id.toString() + " are deleted", Toast.LENGTH_LONG).show()
                }
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    shouldShowDialog.value = false
                }
            ) {
                Text(
                    text = "Discard",
                    color = Color.White
                )
            }
        },
        title = { Text(text = "Add note") },
        text = {
            Text(text = "Are you shor you want to delete this note?")
        }
    )
}