package com.example.nabiz_es;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class veri_kaynagi {

    SQLiteDatabase db;
    sql_katmani bdb;

    public veri_kaynagi(Context c){ bdb=new sql_katmani(c); }

    public void ac(){ db=bdb.getWritableDatabase(); }

    public void kapa(){ bdb.close(); }

    public void akisolustur(olcum a){

        //id autoinc ben vermiyorum o kendi arttıracak
        ContentValues val=new ContentValues();
        val.put("nabiz",a.getNabiz());
        val.put("date",a.getDate());

        db.insert("olcum",null,val);
    }

    public List<olcum> listele(){

        List<olcum>liste=new ArrayList<olcum>();

        String kolonlar[]={"id","nabiz","date"};

        Cursor c = db.query("olcum",kolonlar,null,null,null,null,null);
        c.moveToFirst();

        while (!c.isAfterLast())
        {
            int id =c.getInt(0);
            int nabiz=c.getInt(1);
            String date=c.getString(2);

            //obje olarak kullanacaksak
            olcum a=new olcum(id,nabiz,date);

            //com.example.flowplanner.akis tipinde listeye akisi ekledikk
            liste.add(a);
            c.moveToNext();
        }

        c.close();
        return  liste;
    }

    public void akis_sil()
    {
        /*int id=a.getId();
        db.delete("akis","id="+id,null);*/
        db.delete("olcum",null,null);

    }


}
