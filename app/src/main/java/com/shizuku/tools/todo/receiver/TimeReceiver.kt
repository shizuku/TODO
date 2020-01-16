package com.shizuku.tools.todo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shizuku.tools.todo.service.MessageService

class TimeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClass(context, MessageService::class.java)
        context.startService(intent)
    }
}

