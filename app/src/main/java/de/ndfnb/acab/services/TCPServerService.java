package de.ndfnb.acab.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import de.ndfnb.acab.R;
import de.ndfnb.acab.connections.ServerNetworkPool;
import de.ndfnb.acab.data.LoginRepository;

public class TCPServerService extends Service {

    private LoginRepository loginRepository;
    private ServerSocket serverSocket = null;
    private boolean mRun;
    //fixme poolsize
    private ExecutorService pool = Executors.newFixedThreadPool(10);
    private AtomicBoolean working = new AtomicBoolean(true);
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Socket socket = null;
            mRun = true;
            try {
                serverSocket = new ServerSocket(6969);
                Thread t1 = new Thread(new ServerNetworkPool(pool, serverSocket));
                t1.start();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };


    private void startMeForeground() {
        String NOTIFICATION_CHANNEL_ID = getPackageName();
        String channelName = "Tcp Server Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        Notification.Builder notificationBuilder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tcp Server is running in background")
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.loginRepository = intent.getParcelableExtra("loginRepository");
        return super.onStartCommand(intent, flags, startId);
    }

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
    /**
     * A simple task for sending messages across the network.
     */

}
