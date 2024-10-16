package com.ayslan.project_dm125.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayslan.project_dm125.singleton.PreferencesManager
import com.ayslan.project_dm125.R
import com.ayslan.project_dm125.adapter.TaskItemTouchCallback
import com.ayslan.project_dm125.adapter.TasksAdapter
import com.ayslan.project_dm125.databinding.ActivityMainBinding
import com.ayslan.project_dm125.entity.Task
import com.ayslan.project_dm125.helper.NotificationHelper
import com.ayslan.project_dm125.listener.TaskItemClickListener
import com.ayslan.project_dm125.listener.TaskItemSwipeListener
import com.ayslan.project_dm125.service.TaskService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tasksAdapter: TasksAdapter
    private val taskService: TaskService by lazy {
        ViewModelProvider(this)[TaskService::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        askNotificationPermission()

        if(Firebase.auth.currentUser == null){
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        NotificationHelper(this)//.showNotification("Projeto DM125 Notificação", "Exemplo de notificação")
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        PreferencesManager.updateDailyNotificationPref(this)
        readTasks()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.action_settings){
            startActivity(Intent(this, PreferenceActivity::class.java))
        }

        if(item.itemId == R.id.action_logout){
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initComponents(){
        tasksAdapter = TasksAdapter(binding.tvMessage,this, object : TaskItemClickListener {
            override fun onClick(task: Task) {
                val intent = Intent(this@MainActivity, TaskFormActivity::class.java)
                intent.putExtra("task_id", task.id)
                startActivity(intent)
            }

            override fun onMarkAsCompleClick(position: Int, task: Task) {
                taskService.markAsCompleted(task).observe(this@MainActivity) { responseDto ->
                    if (responseDto.isError) {
                        Toast.makeText(this@MainActivity, "Falha na operação", Toast.LENGTH_SHORT).show()
                    } else {
                        responseDto.value?.let {
                            tasksAdapter.updateItem(position, responseDto.value)
                        }
                    }
                }
            }

            override fun onShareClick(task: Task) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, task.title)
                intent.setType("text/plain")
                startActivity(intent)
            }
        })
        binding.rvTasks.adapter = tasksAdapter
        binding.rvTasks.layoutManager = LinearLayoutManager(this)

        ItemTouchHelper(TaskItemTouchCallback(object : TaskItemSwipeListener {
            override fun onSwipe(position: Int) {
                val task = tasksAdapter.getItem(position)

                // Cria o AlertDialog para confirmar a exclusão
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Você tem certeza que deseja deletar esta tarefa?")
                    .setPositiveButton("Sim") { _, _ ->
                        // Se o usuário confirmar, deleta a tarefa
                        taskService.delete(task).observe(this@MainActivity) { responseDto ->
                            if (responseDto.isError) {
                                tasksAdapter.refreshItem(position)
                            } else {
                                tasksAdapter.deleteItem(position)
                            }
                        }
                    }
                    .setNegativeButton("Não") { dialog, _ ->
                        tasksAdapter.refreshItem(position)
                        dialog.dismiss()
                    }
                    .show()
            }
        })).attachToRecyclerView(binding.rvTasks)


        binding.srlTasks.setOnRefreshListener {
            readTasks()
        }

        binding.fabNewTask.setOnClickListener{
            startActivity(Intent(this, TaskFormActivity::class.java))
        }


    }

    private fun readTasks() {
        taskService.readAll().observe(this) { responseDto ->
            binding.srlTasks.isRefreshing = false

            if (responseDto.isError) {
                Toast.makeText(this, "Erro com o servidor", Toast.LENGTH_SHORT).show()
                binding.tvMessage.text = getString(R.string.get_tasks_error)
            } else {
                responseDto.value?.let { tasks ->
                    tasksAdapter.setItems(tasks)
                }
            }
        }
    }

    private fun askNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED){
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){
                    AlertDialog.Builder(this)
                        .setMessage(R.string.notification_permission_rationale)
                        .setPositiveButton(R.string.accept
                        ) { _, _ ->
                            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton(R.string.ignore, null)
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    //Logica de permissão para uso
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            run {
                if (isGranted) {
                    Log.e("Permission", "Permissão não concedida")
                }
            }
        }
}