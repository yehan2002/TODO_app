package io.github.yehan2002.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.yehan2002.todoapp.database.entities.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(vararg task: Task)

    @Delete
    fun deleteTask(vararg  task: Task);

    @Update
    fun updateTask(vararg  task: Task);

    @Query("SELECT * FROM Task")
    fun  getTasks(): List<Task>

}