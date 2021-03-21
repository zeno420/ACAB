package de.ndfnb.acab.ui.chat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.name = getIntent().getStringExtra("name");
        loginRepository = getIntent().getParcelableExtra("loginRepository");
        try {
            this.routeJSONObject = this.getRoute();
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        try {
            if (this.routeJSONObject != null) {
                tcpClient = new ConnectionManagerTask(ChatActivity.this, this.routeJSONObject.getString("route"), 420187, getApplicationContext()).execute("thanks").get();
            } else {
                //TODO was tun wenn user nicht gefunden wurde. Eigentlich nicht möglich, dass leere Route möglich ist
            }
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        mTextView = (TextView) findViewById(R.id.text);

    }

    private JSONObject getRoute() throws ExecutionException, InterruptedException, JSONException {
        this.routeJSONObject = new APITasks(ChatActivity.this).execute("get_route", loginRepository.getLoggedInUser().getJwtToken(), name).get();
        JSONArray routeArray = this.routeJSONObject.getJSONArray("data");
        try {
            JSONObject routeJSONObject = routeArray.getJSONObject(0);
        } catch (JSONException e) {
            return null;
        }
        return routeJSONObject;
    }

    @Override
    public JSONObject processFinish(JSONObject output) {
        return output;
    }

    @Override
    public TCPClient processFinish(TCPClient output) {
        return null;
    }
}