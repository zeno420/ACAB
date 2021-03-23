package de.ndfnb.acab;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import de.ndfnb.acab.connection.TCPClient;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.service.RouteRefreshService;
import de.ndfnb.acab.service.TCPServerService;
import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.ui.chat.ChatActivity;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetAddress.*;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private ConnectionManagerTask connectionManager;
    public static final String SERVER_IP = "";
    public static final int SERVER_PORT = 1;


    ListView listview;
    LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginRepository = getIntent().getParcelableExtra("loginRepository");
        listview = (ListView) findViewById(R.id.listview);


        //TODO Hier wird der TCPServerService gestartet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), TCPServerService.class));
        } else {
            startService(new Intent(getApplicationContext(), TCPServerService.class));
        }

        String ip = this.getLocalIpV6();
        System.out.println(ip);
        listview.setAdapter(new ContactAdapter(this, new String[]{"zeno420",
                "Nico", "Irgend ein Analphabet", "Damit mein ich Zeno", "LOL"}));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                intent.putExtra("name", listview.getItemAtPosition(position).toString()); //)
                intent.putExtra("loginRepository", loginRepository); //)
                startActivity(intent);

            }
        });
        getSupportActionBar().setTitle("All Chats are Beautiful");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FragmentManager fm = getSupportFragmentManager();

                AddContactDialogFragment addContactDialogFragment = AddContactDialogFragment.newInstance("Some Title", loginRepository);
                addContactDialogFragment.show(fm, "add_contact_dialog_fragment");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getLocalIpV6() {
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
}