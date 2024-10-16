package com.ayslan.project_dm125.repository

import com.ayslan.project_dm125.entity.Task

data class ResponseDto<T>(
    val value: T? = null,
    val isError: Boolean = false
)