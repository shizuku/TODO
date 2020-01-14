package com.shizuku.tools.todo.ui.fragment.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shizuku.tools.todo.Todo
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.data.LogDBOperator

class TodoViewModel : ViewModel() {
    private val op = LogDBOperator(Todo.context)
    var list: MutableLiveData<ArrayList<Log>> = MutableLiveData<ArrayList<Log>>().apply {
        this.value = op.getAllTodo()
    }

    fun update() {
        op.sync()
        list.value = op.getAllTodo()
    }
}
