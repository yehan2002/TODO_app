package io.github.yehan2002.todoapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(vararg task: Task);

    @Query("SELECT * FROM Task")
    fun  getTasks(): Flow<Task>

}