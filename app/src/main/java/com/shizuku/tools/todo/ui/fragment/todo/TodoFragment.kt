package com.shizuku.tools.todo.ui.fragment.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.ui.adapter.LogAdapter

class TodoFragment : Fragment() {
    private lateinit var viewModel: TodoViewModel
    private var list: ArrayList<Log> = ArrayList()
    private lateinit var recycler: RecyclerView
    private lateinit var empty: LinearLayout
    private lateinit var adapter: LogAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var sorter: Int? = 0

    private fun sort() {
        if (sorter == 0) {
            list.sortBy({ it.deadline })
        } else {
            list.sortBy({ it.priority })
        }
    }

    private fun refresh() {
        Thread(Runnable {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            activity?.runOnUiThread {
                viewModel.update()
                swipeRefresh.isRefreshing = false
            }
        }).start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        sorter = bundle?.getInt("sorter")
        viewModel =
            ViewModelProviders.of(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        empty = root.findViewById(R.id.empty_layout)
        recycler = root.findViewById(R.id.frag_recycler)
        recycler.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        adapter = LogAdapter(list)
        recycler.adapter = adapter

        viewModel.list.observe(this, Observer {
            list.clear()
            list.addAll(it)
            if (list.isEmpty()) {
                recycler.visibility = View.INVISIBLE
                empty.visibility = View.VISIBLE
            } else {
                sort()
                recycler.visibility = View.VISIBLE
                empty.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()
        })

        swipeRefresh = root.findViewById(R.id.swipe_refresh)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refresh()
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }
}
