package io.github.yehan2002.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import io.github.yehan2002.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(this.layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addBtn.setOnClickListener { displayDialog(null) }

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.tasks.observe(this) {
            taskAdapter = TaskAdapter(this, it)
            binding.taskContainer.adapter = taskAdapter
            binding.taskContainer.layoutManager = LinearLayoutManager(this)
        }

        val db = TaskDatabase.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            val data = db.taskDao().getTasks()
            runOnUiThread {
                viewModel.setTasks(data)
            }
        }
    }

    fun displayDialog(task: Task?) {
        val dialog = CreateDialogFragment(this, task)
        dialog.show(supportFragmentManager, CreateDialogFragment.TAG)
    }

    fun updateTasks() {
        val db = TaskDatabase.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = db.taskDao().getTasks()
            runOnUiThread {
                viewModel.setTasks(tasks)
            }
        }
    }
}