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
public class ChatActivity extends AppCompatActivity  {
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



        mTextView = (TextView) findViewById(R.id.text);

    }




}