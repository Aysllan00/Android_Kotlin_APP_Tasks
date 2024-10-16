package com.ayslan.project_dm125.service

import com.ayslan.project_dm125.adapter.LocalDateAdapter
import com.ayslan.project_dm125.adapter.LocalTimeAdapter
import com.ayslan.project_dm125.repository.TaskRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime

class RetrofitService {

    private var taskRepository: TaskRepository

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.195:8080/")
            .client(configureClient())
            .addConverterFactory(configureConverter())
            .build()

        taskRepository = retrofit.create(TaskRepository::class.java)
    }

    fun getTaskRepository(): TaskRepository {
        return taskRepository
    }

    private fun configureClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    }

    private fun configureConverter(): Converter.Factory {

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
            .create()

        return GsonConverterFactory.create(gson)
    }
}