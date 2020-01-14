package com.shizuku.tools.todo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast

class LogDBOperator(var context: Context) {
    private var dbHelper: LogDBHelper =
        LogDBHelper(context, context.getExternalFilesDir(null)?.path + "/data.db")
    private var db: SQLiteDatabase = dbHelper.writableDatabase

    fun sync() {
        val time = DateTime.current()
        val c = db.rawQuery("select * from todo where finished=0 and deadline<$time", null)
        if (c.moveToFirst()) {
            do {
                update(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority")),
                        1
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
    }

    fun getAllTodo(): ArrayList<Log> {
        val r = arrayListOf<Log>()
        val c = db.rawQuery("select * from todo where finished=0", null)
        if (c.moveToFirst()) {
            do {
                r.add(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority")),
                        c.getInt(c.getColumnIndex("finished"))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return r
    }

    fun getAllDone(): ArrayList<Log> {
        val r = arrayListOf<Log>()
        val c = db.rawQuery("select * from todo where finished=1", null)
        if (c.moveToFirst()) {
            do {
                r.add(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority")),
                        c.getInt(c.getColumnIndex("finished"))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return r
    }

    fun getAllToday(): ArrayList<Log> {
        val r = arrayListOf<Log>()
        val start = DateTime.todayStart()
        val end = DateTime.todayEnd()
        val c = db.rawQuery("select * from todo where deadline>$start and deadline<$end", null)
        if (c.moveToFirst()) {
            do {
                r.add(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority")),
                        c.getInt(c.getColumnIndex("finished"))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return r
    }

    fun query(id: Int): Log {
        val r: Log
        val c = db.rawQuery("select * from todo where id=$id", null)
        if (c.moveToFirst()) {
            r = Log(
                c.getInt(c.getColumnIndex("id")),
                c.getString(c.getColumnIndex("title")),
                c.getString(c.getColumnIndex("describe")),
                c.getLong(c.getColumnIndex("deadline")),
                c.getInt(c.getColumnIndex("priority")),
                c.getInt(c.getColumnIndex("finished"))
            )
        } else {
            r = Log(0, "", "", 0, 0, 0)
        }
        c.close()
        return r
    }

    fun create() {
        try {
            db.execSQL(
                "create table todo (id integer primary key autoincrement,title text,describe text,deadline int,priority int,finished int);"
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun insert(item: Log) {
        try {
            val title = item.title
            val describe = item.describe
            val deadline = item.deadline
            val priority = item.priority
            val finished = item.finished
            db.execSQL(
                "insert into todo ( title, describe, deadline, priority, finished)values( \"$title\", \"$describe\", $deadline, $priority, $finished);"
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun update(item: Log) {
        try {
            val id = item.id
            val title = item.title
            val describe = item.describe
            val deadline = item.deadline
            val priority = item.priority
            val finished = item.finished
            db.execSQL("update todo set title=\"$title\",describe=\"$describe\",deadline=$deadline,priority=$priority ,finished=$finished where id=$id;")
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun delete(id: Int) {
        try {
            db.execSQL("delete from todo where id=$id")
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

}
