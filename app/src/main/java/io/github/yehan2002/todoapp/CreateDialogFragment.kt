package io.github.yehan2002.todoapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateDialogFragment(private val activity: MainActivity, private val task: Task?) : DialogFragment() {
    private lateinit var name :TextView
    private lateinit var desc :TextView
    private lateinit var priority: Spinner
    private lateinit var dateText :TextView
    private lateinit var dateContainer : LinearLayout;
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var cal = Calendar.getInstance()

    private val action = when (task){
        null -> "Create"
        else-> "Edit"
    }
    private val isEdit = task != null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())

        builder.setTitle("$action Task")

        builder.setPositiveButton(action) { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val newTask = Task(
                    null,
                    name.text.toString(),
                    Priority.valueOf(priority.selectedItem.toString()),
                    desc.text.toString(), Date(cal.timeInMillis)
                )
                if (isEdit) {
                    newTask.uid = task!!.uid
                   TaskDatabase.getInstance(activity).taskDao().updateTask(newTask)
                } else {
                    TaskDatabase.getInstance(activity).taskDao().insertTask(newTask)
                }

                activity.updateTasks()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val dialogView = inflater.inflate(R.layout.task_dialog, null)
        builder.setView(dialogView)

        name = dialogView.findViewById(R.id.taskNameInp)
        desc = dialogView.findViewById(R.id.taskDescriptionInp)
        priority = dialogView.findViewById(R.id.taskPriorityInp)
        dateText = dialogView.findViewById(R.id.timeView)
        dateContainer = dialogView.findViewById(R.id.timeContainer)

        if (task != null) {
            name.text = task.name
            desc.text = task.description
            cal.time = task.deadline
            priority.setSelection(task.priority.ordinal)
        }

        dateText.text = dateFormat.format(cal.time)

        val datePicker = DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, day: Int ->
            cal.set(year, month, day)
            dateText.text = dateFormat.format(cal.time)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis


        dateContainer.setOnClickListener { datePicker.show() }

        return builder.create()
    }


    companion object {
        const val TAG = "TaskDialog"
    }

}