package com.xequal2.kotlincompilerplugin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xequal2.kotlincompilerplugin.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val tasks: Flow<List<TaskEntity>>

    init {
        val db = TodoDatabase.getDatabase(application)
        repository = TaskRepository(db.taskDao())
        tasks = repository.tasks
    }

    fun addTask(text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launch { repository.insert(text) }
        }
    }

    fun updateTask(task: TaskEntity, newText: String) {
        if (newText.isNotBlank()) {
            viewModelScope.launch { repository.update(task.copy(text = newText)) }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { repository.delete(task) }
    }
}
