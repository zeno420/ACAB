package de.ndfnb.acab.ui.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.ACAB;
import de.ndfnb.acab.AddContactDialogFragment;
import de.ndfnb.acab.MessageAdapter;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.tasks.ConnectionManagerTask.AsyncTCPClientResponse;
import de.ndfnb.acab.R;
import de.ndfnb.acab.connection.TCPClient;
import de.ndfnb.acab.tasks.APITasks.AsyncAPIResponse;

public class ChatActivity extends AppCompatActivity implements AsyncAPIResponse, AsyncTCPClientResponse {

    private TextView mTextView;
    private TCPClient tcpClient;
    private LoginRepository loginRepository;
    private JSONObject routeJSONObject;
    private String name;
    private ListView listview;
    private String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.name = getIntent().getStringExtra("name");
        loginRepository = getIntent().getParcelableExtra("loginRepository");
        //routeJSONObject = getRoute(name);
        final TextView message_input = (TextView) findViewById(R.id.message_input);
        final Button send_message_btn = (Button) findViewById(R.id.send_message_btn);

        Spinner spinner = (Spinner) findViewById(R.id.seconds_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.seconds_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        listview = findViewById(R.id.listview_chat);
        MessageAdapter messageAdapter = ACAB.getMessageAdaptersMap().get(name);
        listview.setAdapter(messageAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageAdapter.startViewingMsg(position, view);
            }
        });


        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    route = getRoute(name);
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
                //TODO thread nicht an ui element binden, app geblockt bei fail/labńger dauer
                try {
                    //TODO Name mit Doppelpunkt verbieten
                    String timeString = spinner.getSelectedItem().toString();
                    String[] timeStringSplit = timeString.split(" ");
                    int secVisible = Integer.valueOf(timeStringSplit[0]);
                    System.out.println(secVisible);

                    String input = message_input.getText().toString();
                    if (input == null || input.equals("")) return;
                    String message = loginRepository.getLoggedInUser().getDisplayName() + ":" + secVisible + ":" + input;
                    tcpClient = new ConnectionManagerTask(ChatActivity.this, getApplicationContext()).execute(route, "6969", message).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        getSupportActionBar().setTitle(name);

    }

    private String getRoute(String name) throws ExecutionException, InterruptedException, JSONException {
        JSONObject routeJSONObject = new APITasks(ChatActivity.this).execute("get_route", loginRepository.getLoggedInUser().getJwtToken(), name).get();
        if (routeJSONObject.getString("code") == "404") {
            return null;
        }
        return routeJSONObject.getJSONArray("data").getJSONObject(0).getString("route");
    }


    @Override
    public JSONObject processFinish(JSONObject output) {
        return output;
    }

    @Override
    public TCPClient processFinish(TCPClient output) {
        return output;
    }

}