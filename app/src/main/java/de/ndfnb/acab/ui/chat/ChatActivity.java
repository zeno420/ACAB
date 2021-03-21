package de.ndfnb.acab.ui.chat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.tasks.ConnectionManagerTask.AsyncResponse;
import de.ndfnb.acab.R;
import de.ndfnb.acab.connection.TCPClient;

public class ChatActivity extends AppCompatActivity implements AsyncResponse {
    private ConnectionManagerTask connectionManager;
    private TextView mTextView;
    private TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //TODO route abfragen von dem dude
        //TODO hier wird die verbindung zum chat client aufgebaut
        try {
            tcpClient = new ConnectionManagerTask(ChatActivity.this, "host", 420187, getApplicationContext()).execute("thanks").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        mTextView = (TextView) findViewById(R.id.text);

    }

    @Override
    public TCPClient processFinish(TCPClient output) {
        return output;
    }


}