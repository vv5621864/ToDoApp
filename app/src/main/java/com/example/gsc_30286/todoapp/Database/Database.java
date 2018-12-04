package com.example.gsc_30286.todoapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gsc_30286.todoapp.Tasks.Tasks;

public class Database extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "mydatabase.db";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "tasks";
    private static String COLUMN_ID = "id";
    private static String name = "name";
    private static String decription = "decription";
    private static String datetime1 = "datetime1";
    private static String status = "status";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = new StringBuilder().append("CREATE TABLE ").append(TABLE_NAME).append(" ( ").
                append(datetime1).append(" VARCHAR(20) PRIMARY KEY, ").append(name).append(" VARCHAR(20), ").
                append(decription).append(" VARCHAR(100), ").append(status).append(" VARCHAR(15) );").toString();
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addTasks(Tasks tasks) {
        ContentValues values = new ContentValues();
        values.put(name, tasks.getName());
        values.put(decription, tasks.getDescription());
        values.put(datetime1, tasks.getDatetime());
        values.put(status, tasks.getStatus());
        SQLiteDatabase db = getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, null, values);
        if (rowId == -1) {
            Log.e("DATABASE", "Database exception, data not inserted");
            return false;
        }
        return true;
    }

    public Cursor getRemainingTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery(new StringBuilder().append("SELECT * FROM ").append(TABLE_NAME).append(" WHERE ").append(status).append(" != \"DONE\" ").append(" ORDER BY ").append(datetime1).toString(), null);
        } catch (Exception e) {
            Log.e("getremainingtems", "" + e.getMessage());
        }
        return res;
    }

    public Cursor getCompletedTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery(new StringBuilder().append("SELECT * FROM ").append(TABLE_NAME).append(" WHERE ").append(status).append(" = \"DONE\" ").append(" ORDER BY ").append(datetime1).toString(), null);
        } catch (Exception e) {
            Log.e("getcompleteditems", "" + e.getMessage());
        }
        return res;
    }

    public boolean updateData(Tasks tasks, String previoudTimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, tasks.getName());
        values.put(decription, tasks.getDescription());
        values.put(datetime1, tasks.getDatetime());
        db.update(TABLE_NAME, values, "datetime1 = ?", new String[]{previoudTimeStamp});
        return true;
    }

    public boolean deleteRow(String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "datetime1 = ?", new String[]{timestamp});
        return true;
    }

    public boolean doneStatus(String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(status, "DONE");
        db.update(TABLE_NAME, values, "datetime1 = ?", new String[]{timestamp});
        return true;
    }
}
