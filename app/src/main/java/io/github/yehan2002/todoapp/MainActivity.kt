package io.github.yehan2002.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskRV: RecyclerView;
    private lateinit var taskAdapter: TaskAdapter;

    private val tasks = arrayOf(Task(1, "test", 1, "123"),
        Task(1, "test 2", 1, "123"),
        Task(1, "test 3", 1, "123"),
        Task(1, "test 4", 1, "123"),
        Task(1, "test 5", 1, "123"),
        Task(1, "test 6", 1, "123"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskAdapter = TaskAdapter(this, tasks.toList())
        taskRV = findViewById(R.id.task_container);
        taskRV.adapter = taskAdapter
        taskRV.layoutManager = LinearLayoutManager(this)


    }
}