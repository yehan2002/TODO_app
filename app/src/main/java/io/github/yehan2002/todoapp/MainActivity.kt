package io.github.yehan2002.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var taskRV: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var db: TaskDatabase

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


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


        findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            displayDialog(false, null)
        }


        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskRV = findViewById(R.id.task_container)


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

    fun displayDialog(isEdit: Boolean, task: Task?){
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView: View = inflater.inflate(R.layout.task_dialog, null)

        val name = dialogView.findViewById<TextView>(R.id.taskNameInp)
        val desc = dialogView.findViewById<TextView>(R.id.taskDescriptionInp)
        val priority = dialogView.findViewById<Spinner>(R.id.taskPriorityInp)
        val dateText = dialogView.findViewById<TextView>(R.id.timeView)
        val dateContainer = dialogView.findViewById<LinearLayout>(R.id.timeContainer)

        var date = Date()

        if (task != null){
            name.text = task.name
            desc.text = task.description
            date = task.deadline
            priority.setSelection(task.priority.ordinal)
        }

        val cal = Calendar.getInstance();
        cal.timeInMillis = date.time

        dateText.text = dateFormat.format(cal.time);

        val datePicker = DatePickerDialog(this, {
                _: DatePicker, i: Int, i1: Int, i2: Int ->
            cal.set(i,i1,i2)
            dateText.text = dateFormat.format(cal.time);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = cal.timeInMillis

        val type = when (isEdit){true->"Edit";false-> "Create"}

        dateContainer.setOnClickListener { datePicker.show() }

        builder.setView(dialogView)
        builder.setTitle("$type Task")

        builder.setPositiveButton(type) { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val newTask =  Task(
                    null,
                    name.text.toString(),
                    Priority.valueOf(priority.selectedItem.toString()),
                    desc.text.toString(), Date(cal.timeInMillis)
                )
                if (isEdit){
                    newTask.uid = task!!.uid
                    db.taskDao().updateTask(newTask)
                }else{
                    db.taskDao().insertTask(newTask)
                }

               updateTasks()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun updateTasks(){
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = db.taskDao().getTasks()

            runOnUiThread {
                viewModel.setTasks(tasks)
            }
        }
    }
}