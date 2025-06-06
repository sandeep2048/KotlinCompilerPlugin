package com.xequal2.kotlincompilerplugin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xequal2.kotlincompilerplugin.ui.theme.*

data class Task(val id: Int, var text: String)

@Composable
fun TodoApp() {
    var text by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<Int?>(null) }
    val tasks = remember { mutableStateListOf<Task>() }
    var counter by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(if (editingId == null) "New task" else "Edit task") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DarkGray,
                    unfocusedContainerColor = DarkGray,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (text.isNotBlank()) {
                    if (editingId == null) {
                        tasks.add(Task(counter++, text))
                    } else {
                        tasks.find { it.id == editingId }?.let { it.text = text }
                        editingId = null
                    }
                    text = ""
                }
            }) {
                Text(if (editingId == null) "Add" else "Update")
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    onEdit = { editingId = task.id; text = task.text },
                    onDelete = { tasks.remove(task) }
                )
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(DarkGray, MaterialTheme.shapes.medium)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(task.text, color = White, modifier = Modifier.weight(1f))
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = White)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = White)
        }
    }
}
