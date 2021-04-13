package de.ndfnb.acab.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.ACAB;
import de.ndfnb.acab.adapters.MessageAdapter;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.interfaces.AsyncAPIResponse;
import de.ndfnb.acab.interfaces.AsyncTCPClientResponse;
import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.R;
import de.ndfnb.acab.connections.TCPClient;

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
                //TODO senden-thread nicht an ui element binden, app geblockt bei fail/langer dauer
                try {
                    //TODO Name mit Doppelpunkt verbieten

                    String timeString = spinner.getSelectedItem().toString();
                    String[] timeStringSplit = timeString.split(" ");
                    int secVisible = Integer.valueOf(timeStringSplit[0]);
                    System.out.println(secVisible);

                    String input = message_input.getText().toString();
                    if (input == null || input.equals("")) return;
                    if (route == null)
                        Toast.makeText(ChatActivity.this, "user not reachable", Toast.LENGTH_SHORT).show();
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
        String timeString = routeJSONObject.getJSONArray("data").getJSONObject(0).getString("timestamp");
        LocalDateTime now = LocalDateTime.now().minusSeconds(35).minusHours(2);
        LocalDateTime updateTime = convertToLocalDateTime(timeString);
        if (updateTime.isBefore(now)) {
            return null;
        }
        return routeJSONObject.getJSONArray("data").getJSONObject(0).getString("route");
    }


    private static LocalDateTime convertToLocalDateTime(String dateString) {

        dateString = dateString.replaceAll("T", " ");
        dateString = dateString.replaceAll("Z", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(dateString, formatter);

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