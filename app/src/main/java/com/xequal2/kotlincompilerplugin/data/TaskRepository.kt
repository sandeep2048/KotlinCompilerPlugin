package com.xequal2.kotlincompilerplugin.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    val tasks: Flow<List<TaskEntity>> = dao.getTasks()

    suspend fun insert(text: String) {
        dao.insert(TaskEntity(text = text))
    }

    suspend fun update(task: TaskEntity) {
        dao.update(task)
    }

    suspend fun delete(task: TaskEntity) {
        dao.delete(task)
    }
}
