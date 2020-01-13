package com.shizuku.tools.todo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast

class LogDBOperator(var context: Context) {
    private var dbHelper: LogDBHelper =
        LogDBHelper(context, context.getExternalFilesDir(null)?.path + "/data.db")
    private var db: SQLiteDatabase = dbHelper.writableDatabase

    fun sync() {
        val dayStart = DateTime.todayStart()
        val c =
            db.rawQuery("select * from todo where deadline < $dayStart", null)
        if (c.moveToFirst()) {
            do {
                insertDone(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority"))
                    )
                )
                deleteTodo(c.getInt(c.getColumnIndex("id")))
            } while (c.moveToNext())
        }
        c.close()
    }

    fun getAllTodo() = getAll("todo")
    fun getAllDone() = getAll("done")
    fun getAllToday(): ArrayList<Log> {
        val r = arrayListOf<Log>()
        val dayStart = DateTime.todayStart()
        val dayEnd = DateTime.todayEnd()
        val c =
            db.rawQuery("select * from todo where deadline>$dayStart and deadline<$dayEnd", null)
        if (c.moveToFirst()) {
            do {
                r.add(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority"))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return r
    }

    fun createTodo() = create("todo")
    fun createDone() = create("done")
    fun insertTodo(i: Log) = insert("todo", i)
    fun insertDone(i: Log) = insert("done", i)
    fun updateTodo(j: Log) = update("todo", j)
    fun updateDone(j: Log) = update("done", j)
    fun deleteTodo(i: Int) = delete("todo", i)
    fun deleteDone(i: Int) = delete("done", i)

    private fun getAll(name: String): ArrayList<Log> {
        val r = arrayListOf<Log>()
        val c = db.rawQuery("select * from $name", null)
        if (c.moveToFirst()) {
            do {
                r.add(
                    Log(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("describe")),
                        c.getLong(c.getColumnIndex("deadline")),
                        c.getInt(c.getColumnIndex("priority"))
                    )
                )
            } while (c.moveToNext())
        }
        c.close()
        return r
    }

    private fun create(name: String) {
        try {
            db.execSQL(
                "create table $name(id integer primary key autoincrement,title text,describe text,deadline int,priority int);"
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun insert(name: String, item: Log) {
        try {
            val title = item.title
            val describe = item.describe
            val deadline = item.deadline
            val priority = item.priority
            db.execSQL(
                "insert into $name ( title, describe, deadline, priority)values( \"$title\", \"$describe\", $deadline, $priority);"
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun update(name: String, item: Log) {
        try {
            val id = item.id
            val title = item.title
            val describe = item.describe
            val deadline = item.deadline
            val priority = item.priority
            db.execSQL("update $name set title=\"$title\",describe=\"$describe\",deadline=$deadline,priority=$priority where id=$id;")
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun delete(name: String, id: Int) {
        try {
            db.execSQL("delete from $name where id=$id")
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

}
