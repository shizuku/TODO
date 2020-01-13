package com.shizuku.tools.todo.data

data class Log(
    var id: Int,
    var title: String,
    var describe: String,
    var deadline: Long,
    var priority: Int
)
