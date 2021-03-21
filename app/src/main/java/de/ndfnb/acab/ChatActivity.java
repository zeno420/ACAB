package de.ndfnb.acab;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONObject;

import de.ndfnb.acab.ConnectionManagerTask.AsyncResponse;
public class ChatActivity extends AppCompatActivity implements AsyncResponse {
    private ConnectionManagerTask connectionManager;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //TODO route abfragen von dem dude
        //TODO hier wird die verbindung zum chat client aufgebaut
        connectionManager = new ConnectionManagerTask(ChatActivity.this,"host", 420187);
        connectionManager.execute("thanks");
        connectionManager.mTcpClient.sendMessage("Hi");
        mTextView = (TextView) findViewById(R.id.text);

    }

    @Override
    public TCPClient processFinish(TCPClient output) {
        return output;
    }


}