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

package de.ndfnb.acab.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;

import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import de.ndfnb.acab.ACAB;
import de.ndfnb.acab.R;
import de.ndfnb.acab.adapters.ContactAdapter;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.services.RouteRefreshService;
import de.ndfnb.acab.services.TCPServerService;
import de.ndfnb.acab.ui.chat.ChatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    LoginRepository loginRepository;

    //TODO Service stoppen bei verlassen der App

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginRepository = getIntent().getParcelableExtra("loginRepository");
        listview = (ListView) findViewById(R.id.listview);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent tcpIntent = new Intent(getApplicationContext(), TCPServerService.class);
            Intent routeIntent = new Intent(getApplicationContext(), RouteRefreshService.class);
            tcpIntent.putExtra("loginRepository", loginRepository);
            routeIntent.putExtra("loginRepository", loginRepository);
            startForegroundService(tcpIntent);
            startForegroundService(routeIntent);
        } else {
            Intent tcpIntent = new Intent(getApplicationContext(), TCPServerService.class);
            Intent routeIntent = new Intent(getApplicationContext(), RouteRefreshService.class);
            tcpIntent.putExtra("loginRepository", loginRepository);
            routeIntent.putExtra("loginRepository", loginRepository);
            startService(tcpIntent);
            startService(routeIntent);
        }

        ContactAdapter contactAdapter = new ContactAdapter(getBaseContext(), this);
        ACAB.setContactAdapter(contactAdapter);

        listview.setAdapter(ACAB.getContactAdapter());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                //TODO FIX ME  , ? was soll da gefixt werden?
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
}