package io.github.yehan2002.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        val taskDao = db.taskDao()

        findViewById<Toolbar>(R.id.toolbar).title = "Task Manager"

        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val dialogView: View = inflater.inflate(R.layout.task_dialog, null)

            val name = dialogView.findViewById<TextView>(R.id.taskNameInp)
            val desc = dialogView.findViewById<TextView>(R.id.taskDescriptionInp)
            val date = dialogView.findViewById<CalendarView>(R.id.task_date)
            val priority = dialogView.findViewById<Spinner>(R.id.taskPriorityInp)

            date.minDate = Date().time


            builder.setView(dialogView)

            builder.setTitle("Create Task")

            builder.setPositiveButton("Create") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.taskDao().insertTask(
                        Task(
                            null,
                            name.text.toString(),
                            Priority.valueOf(priority.selectedItem.toString()),
                            desc.text.toString(), Date(date.date)
                        )
                    )
                    val data = taskDao.getTasks()
                    runOnUiThread {
                        viewModel.setTasks(data)
                    }
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
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

        taskRV.addItemDecoration(dividerItemDecoration)

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