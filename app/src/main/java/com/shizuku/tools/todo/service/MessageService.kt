package com.shizuku.tools.todo.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.data.LogDBOperator
import com.shizuku.tools.todo.receiver.TimeReceiver
import java.util.*

class MessageService : Service() {
    private lateinit var channelId: String
    private var binder: MessageBinder = MessageBinder()

    companion object {
        var messageId: Int = 0
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    class MessageBinder : Binder()

    override fun onCreate() {
        super.onCreate()
        Log.d("MessageService", "Create")
    }

    private fun setReminder() {
        val sharePreference = getSharedPreferences("MessageDailyTime", 0)
        val hour = sharePreference.getInt("hour", 0)
        val minute = sharePreference.getInt("minute", 0)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(this, 0, Intent(this, TimeReceiver::class.java), 0)
        val plan = Calendar.getInstance()
        val inst = Calendar.getInstance()
        plan.set(
            plan.get(Calendar.YEAR),
            plan.get(Calendar.MONTH),
            plan.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            0
        )
        if (plan.timeInMillis < inst.timeInMillis) {
            plan.set(
                plan.get(Calendar.YEAR),
                plan.get(Calendar.MONTH),
                plan.get(Calendar.DAY_OF_MONTH) + 1,
                hour,
                minute,
                0
            )
            am.set(AlarmManager.RTC_WAKEUP, plan.timeInMillis, pi)
        } else {
            am.set(AlarmManager.RTC_WAKEUP, plan.timeInMillis, pi)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val msg = pref.getBoolean("msg", false)
        val msgTime = pref.getBoolean("msg_daily", false)
        if (msg and msgTime) {
            channelId = getString(R.string.channel_daily_id)
            val j = Intent("android.intent.category.LAUNCHER")
            val i = PendingIntent.getActivity(this, 0, j, 0)
            val titleStart = getString(R.string.message_daily_title_start)
            val titleEnd = getString(R.string.message_daily_title_end)
            val titleNum = LogDBOperator(this).getAllToday().size.toString()
            val con = getString(R.string.message_daily_description)
            val builder =
                NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(titleStart + titleNum + titleEnd).setContentText(con)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(i)
                    .setAutoCancel(true)
            with(NotificationManagerCompat.from(this)) {
                notify(messageId, builder.build())
            }
            messageId++
            setReminder()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("MessageService", "Destroy")
        super.onDestroy()
    }

}
