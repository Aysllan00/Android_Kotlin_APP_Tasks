package com.ayslan.project_dm125.adapter

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayslan.project_dm125.R
import com.ayslan.project_dm125.singleton.PreferencesManager
import com.ayslan.project_dm125.databinding.TaskListItemBinding
import com.ayslan.project_dm125.entity.Task
import com.ayslan.project_dm125.listener.TaskItemClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TaskViewHolder(
    private val context: Context,
    private val binding: TaskListItemBinding,
    private val listener: TaskItemClickListener) : RecyclerView.ViewHolder(binding.root) {

    fun setValues(task: Task){
        val showDailyNotification = PreferencesManager.getDailyNotificationPref()
        Log.e("valor", "Teste: ${showDailyNotification}")

        if (task.completed) {
            binding.tvLeftBox.background = ContextCompat.getDrawable(binding.root.context, R.drawable.rounded_box_green)
        } else {
            val currentDate = LocalDate.now()
            task.date?.let { taskDate ->
                when {
                    taskDate.isBefore(currentDate.minusDays(1)) -> {
                        // Tarefa vencida (anterior a ontem)
                        binding.tvLeftBox.background = ContextCompat.getDrawable(context, R.drawable.rounded_box_red)
                    }
                    taskDate.isEqual(currentDate) -> {
                        // Tarefa que vence hoje
                        binding.tvLeftBox.background = ContextCompat.getDrawable(context, R.drawable.rounded_box_yellow)
                    }
                    else -> {
                        // Tarefa sem data ou ainda no prazo
                        binding.tvLeftBox.background = ContextCompat.getDrawable(context, R.drawable.rounded_box_blue)
                    }
                }
            } ?: run {
                // Tarefa sem data
                binding.tvLeftBox.background = ContextCompat.getDrawable(context, R.drawable.rounded_box_blue)
            }
        }

        binding.tvTitle.text = task.title

        task.date?.let {
            if (!showDailyNotification) {
                binding.tvDate.text = task.date.toString()
            } else {
                val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
                val formattedDate = task.date!!.format(formatter)
                binding.tvDate.text = formattedDate
            }
        }?: run {
            binding.tvDate.text = "°"
        }

        task.time?.let {
            binding.tvTime.text = task.time.toString()
        }?: run {
            binding.tvTime.text = "°"
        }

        task.description?.let {
            binding.tvDescription.text = task.description
        }?: run {
            binding.tvDescription.text = "°"
        }

        binding.root.setOnClickListener{
            listener.onClick(task)
        }

        binding.root.setOnCreateContextMenuListener{ menu, v, menuInfo ->
            menu.add(R.string.mark_as_completed).setOnMenuItemClickListener {
                listener.onMarkAsCompleClick(adapterPosition, task)
                true
            }

            menu.add(context.getString(R.string.share)).setOnMenuItemClickListener {
                listener.onShareClick(task)
                true
            }
        }
    }
}