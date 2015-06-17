package com.example.mike.interfacetrainings;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mike on 12.06.2015.
 */
public class MyService extends Service {

    public static final String MY_ACTION = "com.example.mike.interfacetrainings.MY_ACTION";

    private static final String TAG = MyService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();

    private HashMap<Date, String> hashMap = new HashMap<Date, String>();

    //работает только когда сервис работает в том же потоке что и UI
    public class LocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(internetReceiver);
    }

    public HashMap<Date, String> getHashMap(){
        return hashMap;
    }

    private BroadcastReceiver internetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            int networkType = intent.getExtras().getInt(ConnectivityManager.EXTRA_NETWORK_TYPE);
            boolean isWiFi = networkType == ConnectivityManager.TYPE_WIFI;
            boolean isMobile = networkType == ConnectivityManager.TYPE_MOBILE;
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
            boolean isConnected = networkInfo.isConnected();

            if (isWiFi) {
                if (isConnected) {
                    Log.e("TAG", "Wi-Fi - CONNECTED");
                    Date date = new Date();
                    hashMap.put(date, "Wi-Fi - CONNECTED");
                } else {
                    Log.e("TAG", "Wi-Fi - DISCONNECTED");
                    Date date = new Date();
                    hashMap.put(date, "Wi-Fi - DISCONNECTED");
                }
            } else if (isMobile) {
                if (isConnected) {
                    Log.e("TAG", "Mobile - CONNECTED");
                    Date date = new Date();
                    hashMap.put(date, "Mobile - CONNECTED");
                } else {
                    Log.e("TAG", "Mobile - DISCONNECTED");
                    Date date = new Date();
                    hashMap.put(date, "Mobile - DISCONNECTED");
                }
            } else {
                if (isConnected) {
                    Log.e("TAG", networkInfo.getTypeName() + " - CONNECTED");
                } else {
                    Log.e("TAG", networkInfo.getTypeName() + " - DISCONNECTED");
                }
            }



            //String action = intent.getAction();

            //здесь пишутся значения в HashMap

//            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
//                Log.e("MyBroadcast INNER", "network changed");
//            }
        }
    };

}
