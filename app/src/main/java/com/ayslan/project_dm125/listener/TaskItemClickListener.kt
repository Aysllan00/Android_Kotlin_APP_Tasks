package com.ayslan.project_dm125.listener

import com.ayslan.project_dm125.entity.Task

interface TaskItemClickListener {

    fun onClick(task: Task)

    fun onMarkAsCompleClick(position: Int, task: Task)

    fun onShareClick(task: Task)
}