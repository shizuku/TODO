package com.shizuku.tools.todo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.data.LogDBOperator
import com.shizuku.tools.todo.ui.fragment.done.DoneFragment
import com.shizuku.tools.todo.ui.fragment.today.TodayFragment
import com.shizuku.tools.todo.ui.fragment.todo.TodoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var op: LogDBOperator
    var sorter: Int = 0
    var f: Int = 1
    private lateinit var appBarTitle: TextView

    private fun replaceFragment(fragment: Fragment) {
        when (fragment) {
            is TodayFragment -> f = 1
            is TodoFragment -> f = 2
            is DoneFragment -> f = 3
        }
        val bundle = Bundle()
        bundle.putInt("sorter", sorter)
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frag_main, fragment)
        transaction.commit()
    }

    private fun firstRun() {
        val sharePreference = getSharedPreferences("FirstRun", 0)
        val firstRun = sharePreference.getBoolean("First", true)
        if (firstRun) {
            op.create()
            sharePreference.edit().putBoolean("First", false).apply()
            return
        } else {
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        op = LogDBOperator(this)
        firstRun()

        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        navView.setCheckedItem(R.id.nav_today)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_today -> {
                    appBarTitle.text = resources.getString(R.string.title_today)
                    replaceFragment(TodayFragment())
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_todo -> {
                    appBarTitle.text = resources.getString(R.string.title_todo)
                    replaceFragment(TodoFragment())
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_done -> {
                    appBarTitle.text = resources.getString(R.string.title_done)
                    replaceFragment(DoneFragment())
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_help -> {
                    val i = Intent(this@MainActivity, HelpActivity::class.java)
                    startActivity(i)
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_settings -> {
                    val i = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(i)
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_feedback -> {
                    val i =
                        Intent(Intent.ACTION_VIEW, Uri.parse("mailto:misakimisakidesu@qq.com"))
                    startActivity(i)
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_rate -> {
                    Toast.makeText(this, getString(R.string.developing), Toast.LENGTH_LONG).show()
                    mDrawerLayout.closeDrawers()
                    true
                }
                R.id.nav_info -> {
                    val i = Intent(this@MainActivity, AboutActivity::class.java)
                    startActivity(i)
                    mDrawerLayout.closeDrawers()
                    true
                }
                else -> {
                    mDrawerLayout.closeDrawers()
                    true
                }
            }
        }

        appBarTitle = findViewById(R.id.app_bar_title)
        appBarTitle.text = resources.getString(R.string.title_today)
        replaceFragment(TodayFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_swap_order -> {
                sorter = when (sorter) {
                    0 -> 1
                    1 -> 0
                    else -> 0
                }
                when (f) {
                    1 -> replaceFragment(TodayFragment())
                    2 -> replaceFragment(TodoFragment())
                    3 -> replaceFragment(DoneFragment())
                }
                true
            }
            R.id.action_add -> {
                val i = Intent("com.shizuku.tools.todo.ADD_TODO")
                startActivity(i)
                true
            }
            else -> true
        }
    }
}
