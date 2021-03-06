package com.gachon.wifi_clinet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    scanActivity mainFrag;
    WifiManager wifiManager;
    TextView test_tv;

    int floor5 = 57;
    int floor4 = 64;
    int floor2 = 39;

    int floor = -1;

    mac_s mac_list = new mac_s();

    ArrayList<String> mac_find = null;
    ArrayList<Integer> rssi_find = null;
    MyHandler handle = new MyHandler(this);

    public List<ScanResult> scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();

        String print_data = "";

        for(int i=0;i<results.size();i++){

            print_data += results.get(i).SSID + " " + results.get(i).BSSID + " " + results.get(i).level + "\n";

        }
        Log.e("wifi-info",print_data);

        test_tv = findViewById(R.id.res_v1);
        test_tv.setText(print_data);

        return results;

    };


    public void scanFailure() {
        ///handle failure
        ///consider using old result
        List<ScanResult> results = wifiManager.getScanResults();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test_tv = findViewById(R.id.res_v1);
        mainFrag = (scanActivity) getSupportFragmentManager().findFragmentById(R.id.scan_activity);


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("permission","checkSelfPermission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Log.d("permission","shouldShowRequestPermissionRationale");
                // ??????????????? ????????? ???????????????.
                // ?????? ????????? ?????? ???????????????.

            } else {
                // ????????????

                Log.d("permission","?????? ??????");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE},
                        1000);

            }
        }
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    List<ScanResult> results = scanSuccess();
                    mac_find = new ArrayList<String>();
                    rssi_find = new ArrayList<Integer>();

                    int arr_size = results.size();

                    for(int i=0;i<arr_size;i++){
                        ScanResult tmp = results.get(i);

                        if(tmp.SSID.compareTo("GC_free_WiFi") == 0){
                            Log.d("TEST","GC_Enable");

                            String mac = tmp.BSSID.substring(9);

                            Log.d("TEST",mac);

                            mac_find.add(
                                    mac
                            );

                            rssi_find.add(
                                    tmp.level
                            );

                        }
                    }

                    find_floor();

                    results.clear();

                    Log.e("wifi","scanSucceed");
                } else {
                    // scan failure handling
                    scanFailure();
                    Log.e("wifi","scanFailed");
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

    }
    //??????????????? ??????????????? ?????????????????? ??????????????? ????????? ?????? ?????? ?????? ?????????
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // ?????? ?????? ??????
                    Log.d("permission","?????? ?????? ??????");

                } else {

                    // ?????? ?????? ??????
                    Log.d("permission","?????? ?????? ??????");
                }
                return;
            }

        }
    }

    public void find_floor(){
        int[] score = new int[3];

        for(int i=0;i<3;i++){
            score[i] = 0;
        }

        for(int i=0;i<mac_find.size();i++){
            int tmp = mac_list.find5Mac(mac_find.get(i));

            if(tmp != -1){
                score[0] ++;
            }

        }

        for(int i=0;i<mac_find.size();i++){
            int tmp = mac_list.find4Mac(mac_find.get(i));

            if(tmp != -1){
                score[1] ++;
            }

        }

        for(int i=0;i<mac_find.size();i++){
            int tmp = mac_list.find2Mac(mac_find.get(i));

            if(tmp != -1){
                score[2] ++;
            }

        }

        int max = -1;

        for(int i=0;i<3;i++){
            if(max < score[i]){
                max = score[i];
                floor = i;
            }
        }

        make_rssi_arr(floor);

    }

    public void make_rssi_arr(int floor){

        if(floor == 0){

            floor = 5;

            int[] rssi_arr = new int[floor5];

            for(int i=0;i<floor5;i++){
                rssi_arr[i] = 0;
            }

            for(int i=0;i<mac_find.size();i++){
                int tmp = mac_list.find5Mac(mac_find.get(i));

                if(tmp != -1){
                    rssi_arr[tmp] = rssi_find.get(i);
                }

            }

            send_rssi_data(floor,rssi_arr);

        }else if(floor == 1){

            floor = 4;

            int[] rssi_arr = new int[floor4];

            for(int i=0;i<floor4;i++){
                rssi_arr[i] = 0;
            }

            for(int i=0;i<mac_find.size();i++){
                int tmp = mac_list.find4Mac(mac_find.get(i));

                if(tmp != -1){
                    rssi_arr[tmp] = rssi_find.get(i);
                }

            }

            send_rssi_data(floor,rssi_arr);

        }else if(floor == 2){

            floor = 2;

            int[] rssi_arr = new int[floor2];

            for(int i=0;i<floor2;i++){
                rssi_arr[i] = 0;
            }

            for(int i=0;i<mac_find.size();i++){
                int tmp = mac_list.find2Mac(mac_find.get(i));

                if(tmp != -1){
                    rssi_arr[tmp] = rssi_find.get(i);
                }

            }

            send_rssi_data(floor,rssi_arr);

        }

    }

    public void send_rssi_data(int floor, int[] arr){

        String tmp_json = "{\n" +
                "\n" +
                "    \"Floor\": "+floor+",\n" +
                "    \"macs\": "+Arrays.toString(arr)+ "\n" +
                "\n" +
                "}";


        Http http = new Http(tmp_json.toString(),handle);

        http.start();

    }

    public void onFragmentChanged(int index) {
        if(index==0){

        } else if(index==1){

            mainFrag = new scanActivity();

            getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFrag).commit();
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity Activity) {
            weakReference = new WeakReference<MainActivity>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = weakReference.get();

            String result;
            TextView result_text = activity.findViewById(R.id.result_text);

            TextView send_result = activity.findViewById(R.id.res_v1);

            if (activity != null) {
                switch (msg.what) {
                    // http ??????????????? JSON ???????????? ???????????? ??????.
                    case 101:

                        result_text.setText("Predict Result : ");

                        result = (String) msg.obj;

                        send_result.setText("your in.... : "+ result);

                        break;
                    // http ??????????????? JSON ???????????? ???????????? ?????? ??????.
                    case 404:

                        result_text.setText("Predict Result : ");

                        result = "Error!";

                        send_result.setText("Result : "+ result);

                        break;

                }
            }
        }


    }
}