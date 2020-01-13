package com.shizuku.tools.todo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.data.DateTime
import com.shizuku.tools.todo.data.Log

class LogAdapter(private var mDataSet: ArrayList<Log>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    class ViewHolder(var v: View) : RecyclerView.ViewHolder(v) {
        private var colors: ArrayList<Int> = arrayListOf(
            ContextCompat.getColor(v.context, R.color.priority_0),
            ContextCompat.getColor(v.context, R.color.priority_1),
            ContextCompat.getColor(v.context, R.color.priority_2)
        )

        var txTitle: TextView = v.findViewById(R.id.log_title)
        var txDateTime: TextView = v.findViewById(R.id.log_date_time)
        var txDescribe: TextView = v.findViewById(R.id.log_describe)
        var imgMain: ImageView = v.findViewById(R.id.log_image)

        fun setColor(c: Int) {
            v.findViewById<View>(R.id.recycler_log_item).setBackgroundColor(colors[c])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_log_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txTitle.text = mDataSet[position].title
        holder.txDateTime.text = DateTime(mDataSet[position].deadline).string()
        holder.txDescribe.text = mDataSet[position].describe
        holder.imgMain.setImageResource(R.drawable.ic_description_gray_24dp)
        holder.setColor(mDataSet[position].priority)
    }

    override fun getItemCount() = mDataSet.size
}
