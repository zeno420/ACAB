package de.ndfnb.acab.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import de.ndfnb.acab.R;
import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.APITasks.AsyncAPIResponse;

// Declare the interface. The method messageReceived(String message) will must be implemented
// in the implementing class
//TODO das ist ein BackgroundService um die routen immer aktuell zu halten
public class RouteRefreshService extends Service implements AsyncAPIResponse {
    @Override
    public JSONObject processFinish(JSONObject output) {
        return output;
    }


    private boolean mRun;
    private AtomicBoolean working = new AtomicBoolean(true);
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Socket socket = null;
            mRun = true;
            while (mRun) {
                try {
                    //TODO get route and compare to current
                    //if difference update new one
                    JSONObject result = new APITasks(RouteRefreshService.this).execute("update_route").get();


                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

    };


    private void startMeForeground() {
        String NOTIFICATION_CHANNEL_ID = getPackageName();
        String channelName = "Route Refresh Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        Notification.Builder notificationBuilder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Route Refresh  is running in background")
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ;

    @Override
    public void onCreate() {
        super.onCreate();
        startMeForeground();
        new Thread(runnable).start();
    }

    @Override
    public void onDestroy() {
        working.set(false);
    }


}
