package de.ndfnb.acab.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.AddContactDialogFragment;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.tasks.ConnectionManagerTask.AsyncTCPClientResponse;
import de.ndfnb.acab.R;
import de.ndfnb.acab.connection.TCPClient;
import de.ndfnb.acab.tasks.APITasks.AsyncAPIResponse;
public class ChatActivity extends AppCompatActivity implements AsyncAPIResponse, AsyncTCPClientResponse{
    private TextView mTextView;
    private TCPClient tcpClient;
    private LoginRepository loginRepository;
    private JSONObject routeJSONObject;
    private String name;
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
                    String message = loginRepository.getLoggedInUser().getDisplayName() + " " + message_input.getText().toString();
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