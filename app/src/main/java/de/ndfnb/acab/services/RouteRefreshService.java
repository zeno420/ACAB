/*
    ACAB (All Chats Are Beautiful)
    Copyright (C) 2021  Zeno Berkhan, Nico Diefenbacher

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package de.ndfnb.acab.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import de.ndfnb.acab.R;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.interfaces.AsyncAPIResponse;
import de.ndfnb.acab.tasks.APITasks;

// Declare the interface. The method messageReceived(String message) will must be implemented
// in the implementing class
// das ist ein BackgroundService um die routen immer aktuell zu halten
public class RouteRefreshService extends Service implements AsyncAPIResponse {

    private LoginRepository loginRepository;

    @Override
    public JSONObject processFinish(JSONObject output) {
        return output;
    }

    private String getLocalIpV6() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isSiteLocalAddress() && inetAddress instanceof Inet6Address) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        return ipaddress;
                    }


                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    private String getLocalIpV4() throws ExecutionException, InterruptedException {
        JSONObject result = new APITasks(RouteRefreshService.this).execute("get_ip").get();
        System.out.println(result);
        try {
            return result.getString("ip");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
                    String ipAdress = getLocalIpV6();
                    if (loginRepository != null && ipAdress != null) {
                        JSONObject result = new APITasks(RouteRefreshService.this).execute("update_route", loginRepository.getLoggedInUser().getJwtToken(), ipAdress).get();
                        String code = null;
                        try {
                            code = result.getString("code");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == null || !code.equals("200")) {
                            Handler mainHandler = new Handler(getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "route not updated", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        System.out.println(result.toString());
                    }
                    SystemClock.sleep(30000);


                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.loginRepository = intent.getParcelableExtra("loginRepository");
        return super.onStartCommand(intent, flags, startId);
    }


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
