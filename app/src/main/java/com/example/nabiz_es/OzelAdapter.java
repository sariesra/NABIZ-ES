package com.example.nabiz_es;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OzelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<olcum>     mKisiListesi;

    public OzelAdapter(Activity activity, List<olcum> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mKisiListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public olcum getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mKisiListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.activity_satir_layout, null);

        TextView textView_isadi =
                (TextView) satirView.findViewById(R.id.isadi);
        TextView textView_deadline =
                (TextView) satirView.findViewById(R.id.deadline);

        olcum akis = mKisiListesi.get(position);

        SpannableString content = new SpannableString(String.format("%d ",akis.getNabiz())+"bpm");
       /* content.setSpan(new UnderlineSpan(), 0, content.length(), 0);*/

        textView_isadi.setText(content);

        textView_deadline.setText(akis.getDate());


       /* textView_isadi.setBackgroundColor(Color.rgb(255, 0, 0));
        textView_deadline.setBackgroundColor(Color.rgb(255, 0, 0));*/

            return satirView;
    }
}
