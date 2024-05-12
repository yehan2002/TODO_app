package io.github.yehan2002.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.yehan2002.todoapp.database.entities.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}