package com.ayslan.project_dm125.activity

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ayslan.project_dm125.databinding.ActivityTaskFormBinding
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.ayslan.project_dm125.entity.Task
import com.ayslan.project_dm125.service.TaskService
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class TaskFormActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTaskFormBinding

    private val taskService: TaskService by lazy {
        ViewModelProvider(this)[TaskService::class.java]
    }

    private var taskId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.extras?.getString(Intent.EXTRA_TEXT)?.let { text ->
            binding.etTitle.setText(text)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        taskId = intent.getLongExtra("task_id", -1L).takeIf { it != -1L }

        if (taskId != null) {
            loadTaskDetails(taskId!!)
        }

        initComponents()
        DateTimeFunction()

    }

    private fun loadTaskDetails(id: Long) {
        taskService.getTaskById(id).observe(this) { responseDto ->
            if (responseDto.isError) {
                Toast.makeText(this, "Erro ao carregar a tarefa", Toast.LENGTH_SHORT).show()
            } else {
                responseDto.value?.let { task ->
                    setValues(task)
                }
            }
        }
    }

    private fun initComponents(){
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val dateText = binding.etDate.text.toString()

            binding.tilTitle.error = null

            if (title.isEmpty()) {
                binding.tilTitle.error = "O título é obrigatório"
                return@setOnClickListener
            }

            val date = if (dateText.isNotEmpty()) {
                // Formato esperado: "dd/MM/yyyy"
                val dateParts = dateText.split("/")
                LocalDate.of(dateParts[2].toInt(), dateParts[1].toInt(), dateParts[0].toInt())
            } else {
                null
            }
            val timeText = binding.etTime.text.toString()
            val time = if (timeText.isNotEmpty()) {
                // Formato esperado: "HH:mm"
                val timeParts = timeText.split(":")
                LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            } else {
                null
            }

            val task = Task(
                title = title,
                description = description,
                date = date,
                time = time,
                id = taskId
            )

            taskService.save(task).observe(this){ responseDto ->
                if(responseDto.isError){
                    Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                } else{
                    finish()
                }
            }
        }
    }

    private fun setValues(task: Task) {
        taskId = task.id
        binding.etTitle.setText(task.title)
        binding.etDescription.setText(task.description)
        task.date?.let { date ->
            val formattedDate = String.format("%02d/%02d/%d", date.dayOfMonth, date.monthValue, date.year)
            binding.etDate.setText(formattedDate)
        }

        task.time?.let { time ->
            val formattedTime = String.format("%02d:%02d", time.hour, time.minute)
            binding.etTime.setText(formattedTime)
        }

        if (task.completed) {
            binding.btnSave.visibility = View.GONE
        }
    }

    private fun DateTimeFunction() {
        // Inicializa o calendário
        val calendar = Calendar.getInstance()

        // Quando o campo de data for clicado
        binding.etDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.etDate.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear))
            }, year, month, day)

            datePickerDialog.show()
        }

        // Quando o campo de hora for clicado
        binding.etTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                binding.etTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
            }, hour, minute, true)

            timePickerDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}