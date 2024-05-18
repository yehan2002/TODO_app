package io.github.yehan2002.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var taskRV: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var db: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = TaskDatabase.getInstance(this)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            displayDialog(null)
        }


        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskRV = findViewById(R.id.task_container)


        viewModel.tasks.observe(this) {
            taskAdapter = TaskAdapter(this, it)
            taskRV.adapter = taskAdapter
            taskRV.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = db.taskDao().getTasks()
            runOnUiThread {
                viewModel.setTasks(data)
            }
        }
    }

    fun displayDialog( task: Task?) {
        CreateDialogFragment(this, task).show(supportFragmentManager, CreateDialogFragment.TAG)
    }

    fun updateTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = db.taskDao().getTasks()

            runOnUiThread {
                viewModel.setTasks(tasks)
            }
        }
    }
}