package com.example.noteandtodolistproject

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun AddNoteDialog(shouldShowDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { shouldShowDialog.value = false },
        confirmButton = {
            Button(
                onClick = {
                    shouldShowDialog.value = false
                }
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White
                )
            }
        },
        title = { Text(text = "Add note") },
        text = {
            Column {
                var title by remember { mutableStateOf(TextFieldValue("")) }
                var note by remember { mutableStateOf(TextFieldValue("")) }
                TextField(value = title, onValueChange = {
                    title = it
                },
                    label = { Text(text = "Title") },
                    placeholder = { Text(text = "Title") }
                )
                TextField(value = note, onValueChange = {
                                                        note = it
                },
                    label = { Text(text = "Note") },
                    placeholder = { Text(text = "Note")},
                    modifier = Modifier.height(300.dp)
                        .padding(top = 10.dp)
                )
            }
        }
    )
}