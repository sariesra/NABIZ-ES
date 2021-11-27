package com.example.nabiz_es;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql_katmani extends SQLiteOpenHelper {

    public sql_katmani(Context c)
    {
        super(c,"olcum",null,1);
    }


    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table olcum(id integer primary key autoincrement ,nabiz integer,date text)");

    }

    public void onUpgrade(SQLiteDatabase db,int eski,int yeni)
    {
        db.execSQL("drop table if exists olcum");

    }

}
