package com.shizuku.tools.todo.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.Todo
import com.shizuku.tools.todo.data.DateTime
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.data.LogDBOperator

import kotlinx.android.synthetic.main.activity_view.*
import java.util.*

class ViewActivity : AppCompatActivity() {
    private lateinit var log: Log
    private val op = LogDBOperator(Todo.context)
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton0: RadioButton
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var linearLayout: LinearLayout
    private lateinit var dataTime: DateTime
    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_back_white_24dp)

        val id = intent.getIntExtra("id", 0)
        log = op.query(id)
        dataTime = DateTime(log.deadline)

        linearLayout = findViewById(R.id.content_add)
        editTextTitle = findViewById(R.id.edit_add_title)
        editTextDescription = findViewById((R.id.edit_add_description))
        radioGroup = findViewById(R.id.radio_group)
        radioButton0 = findViewById(R.id.radio_0)
        radioButton1 = findViewById(R.id.radio_1)
        radioButton2 = findViewById(R.id.radio_2)
        dateText = findViewById(R.id.text_choose_date)
        timeText = findViewById(R.id.text_choose_time)

        linearLayout.setBackgroundColor(
            when (log.priority) {
                0 -> getColor(R.color.priority_0)
                1 -> getColor(R.color.priority_1)
                2 -> getColor(R.color.priority_2)
                else -> getColor(R.color.priority_0)
            }
        )

        editTextTitle.setText(log.title)
        editTextDescription.setText(log.describe)
        radioGroup.check(
            when (log.priority) {
                0 -> R.id.radio_0
                1 -> R.id.radio_1
                2 -> R.id.radio_2
                else -> R.id.radio_0
            }
        )
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_ok -> {
                onOK()
                true
            }
            R.id.action_delete -> {
                onDelete()
                true
            }
            else -> true
        }
    }

    private fun onOK() {
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
            op.update(
                Log(
                    log.id,
                    title,
                    editTextDescription.text.toString(),
                    dataTime.num(),
                    pri, log.finished
                )
            )
        }
        finish()
    }

    private fun onDelete() {
        val dialog = AlertDialog.Builder(this)
        dialog.setOnCancelListener { }
        dialog.setOnDismissListener { }
        dialog.setIcon(R.drawable.ic_dialog_warning_24dp)
            .setTitle(R.string.dialog_warning_title)
            .setMessage(R.string.dialog_warning_msg)
            .setCancelable(true)
            .setPositiveButton(
                R.string.dialog_warning_button_pos,
                DialogInterface.OnClickListener { _, _ ->
                    op.delete(log.id)
                    finish()
                }
            ).setNegativeButton(
                R.string.dialog_warning_button_neg,
                DialogInterface.OnClickListener { _, _ -> })
        dialog.show()
    }

}
