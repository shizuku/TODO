package com.shizuku.tools.todo.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.circularreveal.CircularRevealLinearLayout
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.data.DateTime
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.data.LogDBOperator

import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton0: RadioButton
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var linearLayout: LinearLayout
    private var dataTime = DateTime()
    private lateinit var timeText: TextView
    private lateinit var dateText: TextView

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        linearLayout = findViewById(R.id.content_add)
        linearLayout.setBackgroundColor(getColor(R.color.priority_0))
        editTextTitle = findViewById(R.id.edit_add_title)
        editTextDescription = findViewById((R.id.edit_add_description))

        radioGroup = findViewById(R.id.radio_group)
        radioButton0 = findViewById(R.id.radio_0)
        radioButton1 = findViewById(R.id.radio_1)
        radioButton2 = findViewById(R.id.radio_2)
        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.radio_0 -> {
                    linearLayout.setBackgroundColor(getColor(R.color.priority_0))
                }
                R.id.radio_1 -> {
                    linearLayout.setBackgroundColor(getColor(R.color.priority_1))
                }
                R.id.radio_2 -> {
                    linearLayout.setBackgroundColor(getColor(R.color.priority_2))
                }
                else -> {
                }
            }
        }
        dateText = findViewById(R.id.text_choose_date)
        timeText = findViewById(R.id.text_choose_time)
        dateText.text = dataTime.dateString()
        timeText.text = dataTime.timeString()

        dateText.setOnClickListener {
            DatePickerDialog(
                this,
                0,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dataTime.set(year, month, dayOfMonth)
                    dateText.text = dataTime.dateString()
                },
                dataTime.cal.get(Calendar.YEAR),
                dataTime.cal.get(Calendar.MONTH),
                dataTime.cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        timeText.setOnClickListener {
            TimePickerDialog(
                this, 4,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    dataTime.set(hourOfDay, minute)
                    timeText.text = dataTime.timeString()
                }, dataTime.cal.get(Calendar.HOUR_OF_DAY),
                dataTime.cal.get(Calendar.MINUTE), true
            ).show()
        }
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_back_white_24dp)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_ok -> {
                val pri: Int = when (radioGroup.checkedRadioButtonId) {
                    R.id.radio_0 -> 0
                    R.id.radio_1 -> 1
                    R.id.radio_2 -> 2
                    else -> 0
                }
                val op = LogDBOperator(this)
                val title = editTextTitle.text.toString()
                if (title.isEmpty()) {
                    Snackbar.make(linearLayout, R.string.msg_add_title_empty, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    op.insertTodo(
                        Log(
                            0,
                            title,
                            editTextDescription.text.toString(),
                            dataTime.num(),
                            pri
                        )
                    )
                    op.sync()
                    finish()
                }
                true
            }
            else -> true
        }
    }
}
