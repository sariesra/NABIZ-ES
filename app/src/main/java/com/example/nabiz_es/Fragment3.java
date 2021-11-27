package com.example.nabiz_es;

import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment3 extends Fragment{

    @Nullable
    @Override
    public View  onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.layout_uc,container,false);

        return  view;



    }

}
