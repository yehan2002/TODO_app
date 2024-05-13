package io.github.yehan2002.todoapp

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
    val taskName: TextView = view.findViewById(R.id.taskName)
    val taskPriority: TextView = view.findViewById(R.id.taskPriority)
    val taskDate: TextView = view.findViewById(R.id.taskDeadline)
    val taskDesc: TextView  = view.findViewById(R.id.task_desc)

    val taskExpander: AppCompatImageView = view.findViewById(R.id.expandBtn);
    val expanderView: LinearLayout = view.findViewById(R.id.expandContent);

    val editBtn: AppCompatImageButton = view.findViewById(R.id.edit_btn);
    val delBtn: AppCompatImageButton = view.findViewById(R.id.delBtn);
}