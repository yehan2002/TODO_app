package io.github.yehan2002.todoapp

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(vararg task: Task);
}