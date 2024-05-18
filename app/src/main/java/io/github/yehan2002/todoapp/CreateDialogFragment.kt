package io.github.yehan2002.todoapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import io.github.yehan2002.todoapp.databinding.TaskDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateDialogFragment(private val activity: MainActivity, private val task: Task?) : DialogFragment() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var selectedDate = Calendar.getInstance()
    private lateinit var  binding: TaskDialogBinding

    private val action = when (task){
        null -> "Create"
        else-> "Edit"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = TaskDialogBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("$action Task")
        builder.setView(binding.root)

        builder.setPositiveButton(action) { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val newTask = Task(
                    null,
                    binding.taskNameInp.text.toString(),
                    Priority.valueOf(binding.taskPriorityInp.selectedItem.toString()),
                    binding.taskDescriptionInp.text.toString(), Date(selectedDate.timeInMillis)
                )

                if (task != null) {
                    newTask.uid = task.uid
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


        if (task != null) {
            binding.taskNameInp.setText(task.name)
            binding.taskDescriptionInp.setText(task.description)
            binding.taskPriorityInp.setSelection(task.priority.ordinal)

            selectedDate.time = task.deadline
        }

        binding.timeView.text = dateFormat.format(selectedDate.time)

        val datePicker = DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, day: Int ->
            selectedDate.set(year, month, day)
            binding.timeView.text = dateFormat.format(selectedDate.time)
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH))

        datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis


        binding.timeContainer.setOnClickListener { datePicker.show() }

        return builder.create()
    }


    companion object {
        const val TAG = "TaskDialog"
    }

}