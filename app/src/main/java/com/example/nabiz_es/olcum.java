package com.example.nabiz_es;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class olcum {


        int id;
        int nabiz;
        String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNabiz() {
        return nabiz;
    }

    public void setNabiz(int nabiz) {
        this.nabiz = nabiz;
    }


        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public olcum(int id,int nabiz,String date) {
            this.id = id;
            this.nabiz=nabiz;
            this.date=date;
        }

        public String toString(){

            return ""+nabiz+"bpm"+date ;
        }




}
