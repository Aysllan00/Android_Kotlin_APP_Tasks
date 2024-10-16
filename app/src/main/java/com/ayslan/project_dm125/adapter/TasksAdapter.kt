package com.ayslan.project_dm125.adapter

import android.content.Context
import android.provider.Settings.System.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayslan.project_dm125.R
import com.ayslan.project_dm125.databinding.TaskListItemBinding
import com.ayslan.project_dm125.entity.Task
import com.ayslan.project_dm125.listener.TaskItemClickListener

class TasksAdapter(
    private val messageView: TextView,
    private val context: Context,
                   private val listener: TaskItemClickListener
) : RecyclerView.Adapter<TaskViewHolder>() {

    private val tasks = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.e("Adapter", "Criando um TaskViwHolder")
        val binding = TaskListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskViewHolder(context, binding, listener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, position: Int) {
        Log.e("Adapter", "Instanciando um TaskViwHolder")
        val task = tasks[position]
        taskViewHolder.setValues(task)
    }

    fun setItems(items: List<Task>){
        tasks.clear()
        tasks.addAll(items)

        notifyDataSetChanged()
        checkEmptyTasks()
    }

    fun getItem(position: Int): Task{
        return tasks[position]
    }

    fun refreshItem(position: Int){
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int){
        tasks.removeAt(position)
        notifyItemRemoved(position)
        checkEmptyTasks()
    }

    fun updateItem(position: Int, item: Task) {
        tasks[position] = item
        notifyItemChanged(position)
    }

    private fun checkEmptyTasks(){
        if(tasks.isEmpty()){
            messageView.text = context.getString(R.string.no_tasks)
        }
        else{
            messageView.text = null
        }
    }
}