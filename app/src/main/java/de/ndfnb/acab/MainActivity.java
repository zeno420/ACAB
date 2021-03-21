package de.ndfnb.acab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import de.ndfnb.acab.data.LoginRepository;


public class MainActivity extends AppCompatActivity {

    private ConnectionManagerTask connectionManager;
    public static final String SERVER_IP = "" ;
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

        listview.setAdapter(new ContactAdapter(this, new String[] { "Zeno",
                "Nico", "Irgend ein Analphabet", "Damit mein ich Zeno", "LOL" }));
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
}