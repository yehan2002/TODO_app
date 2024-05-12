package io.github.yehan2002.todoapp

import android.os.Bundle
import android.widget.CalendarView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var taskRV: RecyclerView
    private lateinit var taskAdapter: TaskAdapter;

    private lateinit var db: TaskDatabase ;


    private val tasks = arrayOf(
        Task(1, "test", Priority.Normal, "123", Date()),
        Task(2, "test 2", Priority.Normal, "123", Date()),
        Task(3, "test 3", Priority.Normal, "123", Date()),
        Task(4, "test 4", Priority.Normal, "123", Date()),
        Task(5, "test 5", Priority.Normal, "123", Date()),
        Task(6, "test 6", Priority.Normal, "123", Date())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db =TaskDatabase.getInstance(this)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val taskDao = db.taskDao()
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.insertTask(*tasks)
        }

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.task_dialog);

            builder.setTitle("Create Task")

            builder.setPositiveButton("Create") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {

                }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }


        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskRV = findViewById(R.id.task_container)

        val dividerItemDecoration = DividerItemDecoration(
            taskRV.context,
            LinearLayout.VERTICAL
        )

        taskRV.addItemDecoration(dividerItemDecoration);

        viewModel.tasks.observe(this) {
            taskAdapter = TaskAdapter(this, it, viewModel)
            taskRV.adapter = taskAdapter
            taskRV.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = taskDao.getTasks()
            runOnUiThread {
                viewModel.setTasks(data)
            }
        }
    }


}