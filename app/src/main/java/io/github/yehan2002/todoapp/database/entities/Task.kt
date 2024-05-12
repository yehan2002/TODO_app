package io.github.yehan2002.todoapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task(
    @PrimaryKey
    val uid: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name="priority")
    val priority: Int,

    @ColumnInfo(name="description")
    val description: String,

//    @ColumnInfo(name="deadline")
//    val deadline: Date
)