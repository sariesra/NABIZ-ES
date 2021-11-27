package com.example.nabiz_es;

import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Fragment2 extends Fragment  {

    veri_kaynagi vk;

    @Nullable
    @Override
    public View  onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.layout_iki,container,false);

        ListView listview =(ListView)view.findViewById(R.id.list);

        vk=new veri_kaynagi(getActivity());
        vk.ac();//veritabanı baglantısı acık simdi baglancaz

        List<olcum> akıslar=vk.listele();
        final ArrayAdapter<olcum> adaptor=new ArrayAdapter<olcum>(getActivity(),android.R.layout.simple_list_item_1,akıslar);
        listview.setAdapter(adaptor);


        final OzelAdapter adaptorumuz=new OzelAdapter(getActivity(), akıslar);
        final ListView listee=view.findViewById(R.id.list);

        listee.setAdapter(adaptorumuz);

        view.findViewById(R.id.sil_butonu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vk.akis_sil();
                adaptor.clear();
                adaptor.notifyDataSetChanged();
                adaptorumuz.notifyDataSetChanged();

            }
        });

        return  view;


    }



}
