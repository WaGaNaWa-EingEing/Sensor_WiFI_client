package com.gachon.wifi_clinet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class scanActivity extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.scan_activity,container,false);

        Button send_btn = rootView.findViewById(R.id.where);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();

                boolean success = activity.wifiManager.startScan();
                if(!success){
                    activity.scanFailure();
                    Log.e("wifi","scanFailed");
                }

            }
        });
        return rootView;
    }
}