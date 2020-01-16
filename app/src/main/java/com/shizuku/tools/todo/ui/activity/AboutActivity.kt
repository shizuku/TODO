package com.shizuku.tools.todo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.shizuku.tools.todo.R

import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    private lateinit var license: TextView
    private lateinit var thanks: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_back_white_24dp)

        license = findViewById(R.id.text_license)
        license.setOnClickListener {
            val i = Intent(this, LicenseActivity::class.java)
            startActivity(i)
        }
        /*thanks = findViewById(R.id.text_thanks)
        thanks.setOnClickListener {
            val i = Intent(this, ThanksActivity::class.java)
            startActivity(i)
        }
         */
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
