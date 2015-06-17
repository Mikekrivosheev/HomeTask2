package com.example.mike.interfacetrainings;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends Activity {

    private MyBroadcastReceiver myBroadcastReceiver;

    private LinearLayout mainL;
    private float currentAlpha = 1.0f;
    private  boolean isStarted = false;
    private boolean isUp = false;
    private Thread thread;
    private Button button;
    private TextView log;

    private MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainL = (LinearLayout)findViewById(R.id.mainL);
        button = (Button)findViewById(R.id.button1);
        log = (TextView)findViewById(R.id.log);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(MainActivity.this, MyService.class));
       bindService(new Intent(MainActivity.this, MyService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("ACTIVITY", "Activity connected to service");

            MyService.LocalBinder localBinder = (MyService.LocalBinder) service;

            myService = localBinder.getService();

            HashMap<Date, String> hashMap = myService.getHashMap();

            Iterator<Map.Entry<Date, String>> it = hashMap.entrySet().iterator();

            while (it.hasNext()){
                Map.Entry<Date, String> pp = it.next();

                Date keyDate = pp.getKey();
                String state = pp.getValue();
                log.setText("Date: " + keyDate.toString() + "; State: " + state);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
           Log.e("ACTIVITY", "Activity disconnected from service");
        }
    };

}


