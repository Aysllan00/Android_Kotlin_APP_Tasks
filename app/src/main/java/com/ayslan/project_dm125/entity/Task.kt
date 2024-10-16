package com.ayslan.project_dm125.entity

import android.app.ActivityManager.TaskDescription
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class Task(
    var id: Long? = null,
    var title: String,
    var date: LocalDate? = null,
    var time: LocalTime? = null,
    var description: String? = null,
    val completed: Boolean = false
) :Serializable