package com.shizuku.tools.todo.ui.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.todo
import kotlinx.android.synthetic.main.activity_help.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_back_white_24dp)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val time = findPreference<Preference>("msg_daily_time")
            val sharePreference = todo.context.getSharedPreferences("MessageDailyTime", 0)
            var hour = sharePreference.getInt("hour", 0)
            var minute = sharePreference.getInt("minute", 0)
            if (minute <= 9) {
                time?.summary = "$hour:0$minute"
            } else {
                time?.summary = "$hour:$minute"
            }
            time?.setOnPreferenceClickListener {
                TimePickerDialog(
                    context, 0,
                    TimePickerDialog.OnTimeSetListener { _, h, m ->
                        sharePreference.edit().putInt("hour", h).putInt("minute", m).apply()
                        hour = sharePreference.getInt("hour", 0)
                        minute = sharePreference.getInt("minute", 0)
                        if (minute <= 9) {
                            time.summary = "$hour:0$minute"
                        } else {
                            time.summary = "$hour:$minute"
                        }
                    }, hour,
                    minute, true
                ).show()
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

}
