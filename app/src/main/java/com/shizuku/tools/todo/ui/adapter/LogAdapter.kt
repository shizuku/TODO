package com.shizuku.tools.todo.ui.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shizuku.tools.todo.R
import com.shizuku.tools.todo.Todo
import com.shizuku.tools.todo.data.DateTime
import com.shizuku.tools.todo.data.Log
import com.shizuku.tools.todo.data.LogDBOperator
import com.shizuku.tools.todo.ui.activity.ViewActivity

class LogAdapter(private var mDataSet: ArrayList<Log>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    class ViewHolder(var v: View) : RecyclerView.ViewHolder(v) {
        private var colors: ArrayList<Int> = arrayListOf(
            ContextCompat.getColor(v.context, R.color.priority_0),
            ContextCompat.getColor(v.context, R.color.priority_1),
            ContextCompat.getColor(v.context, R.color.priority_2),
            ContextCompat.getColor(v.context, R.color.priority_3)
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
        val holder = ViewHolder(v)
        holder.v.setOnClickListener {
            val position = holder.adapterPosition
            val log = mDataSet[position]
            val i = Intent(it.context, ViewActivity::class.java)
            i.putExtra("id", log.id)
            it.context.startActivity(i)
        }
        holder.imgMain.setOnClickListener {
            val position = holder.adapterPosition
            val log = mDataSet[position]
            if (log.finished == 0) {
                val dialog = AlertDialog.Builder(it.context)
                dialog.setOnCancelListener { }
                dialog.setOnDismissListener { }
                dialog.setIcon(R.drawable.ic_dialog_ask_24dp)
                    .setTitle(R.string.dialog_finish_title)
                    .setMessage(R.string.dialog_finish_msg)
                    .setCancelable(true)
                    .setPositiveButton(
                        R.string.dialog_warning_button_pos,
                        DialogInterface.OnClickListener { _, _ ->
                            val op = LogDBOperator(Todo.context)
                            op.update(
                                Log(
                                    log.id,
                                    log.title,
                                    log.describe,
                                    log.deadline,
                                    log.priority,
                                    1
                                )
                            )
                        }
                    ).setNegativeButton(
                        R.string.dialog_warning_button_neg,
                        DialogInterface.OnClickListener { _, _ -> })
                dialog.show()
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txTitle.text = mDataSet[position].title
        holder.txDateTime.text = DateTime(mDataSet[position].deadline).string()
        holder.txDescribe.text = mDataSet[position].describe
        when (mDataSet[position].finished) {
            0 -> holder.imgMain.setImageResource(R.drawable.ic_log_adapter_unfinished)
            else -> holder.imgMain.setImageResource(R.drawable.ic_log_adapter_finished)
        }
        holder.setColor(mDataSet[position].priority)
    }

    override fun getItemCount() = mDataSet.size
}
