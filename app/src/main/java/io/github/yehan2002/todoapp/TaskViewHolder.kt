package io.github.yehan2002.todoapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
    val taskName: TextView = view.findViewById(R.id.taskName);
    val taskPriority: TextView = view.findViewById(R.id.taskPriority);
    val taskDate: TextView = view.findViewById(R.id.taskDeadline);
}