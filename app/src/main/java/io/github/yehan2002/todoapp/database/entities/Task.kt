package io.github.yehan2002.todoapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.github.yehan2002.todoapp.DataConverters
import io.github.yehan2002.todoapp.Priority
import java.util.Date

@Entity
@TypeConverters(DataConverters::class)
data class Task(
    @PrimaryKey
    val uid: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name="priority")
    val priority: Priority,

    @ColumnInfo(name="description")
    val description: String,

    @ColumnInfo(name="deadline")
    val deadline: Date
)