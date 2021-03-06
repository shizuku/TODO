package com.shizuku.tools.todo.ui.fragment.today

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.data.LogDBOperator
import com.shizuku.tools.todo.todo

class TodayViewModel : ViewModel() {
    private val op = LogDBOperator(todo.context)
    var list: MutableLiveData<ArrayList<Log>> = MutableLiveData<ArrayList<Log>>().apply {
        this.value = op.getAllToday()
    }

    fun update() {
        op.sync()
        list.value = op.getAllToday()
    }
}
