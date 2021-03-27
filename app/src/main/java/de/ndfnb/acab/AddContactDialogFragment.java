package de.ndfnb.acab;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.connection.TCPClient;
import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.APITasks.AsyncAPIResponse;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.tasks.ConnectionManagerTask;
import de.ndfnb.acab.tasks.ConnectionManagerTask.AsyncTCPClientResponse;
import de.ndfnb.acab.tasks.ConnectionManagerTask.*;
import de.ndfnb.acab.ui.chat.ChatActivity;

public class AddContactDialogFragment extends DialogFragment implements AsyncAPIResponse, AsyncTCPClientResponse {
    private EditText mEditText;
    private LoginRepository loginRepository;
    private ConnectionManagerTask connectionManagerTask;
    //private JSONObject routeJSONObject;
    private TCPClient tcpClient;

    public static AddContactDialogFragment newInstance(String title, LoginRepository loginRepository) {
        AddContactDialogFragment frag = new AddContactDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("loginRepository", (Parcelable) loginRepository);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(800, 600);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loginRepository = getArguments().getParcelable("loginRepository");


    }

    /*private JSONObject getRoute(String name) throws ExecutionException, InterruptedException, JSONException {
        this.routeJSONObject = new APITasks(AddContactDialogFragment.this).execute("get_route", loginRepository.getLoggedInUser().getJwtToken(), name).get();
        JSONArray routeArray = this.routeJSONObject.getJSONArray("data");
        try {
            JSONObject routeJSONObject = routeArray.getJSONObject(0);
        } catch (JSONException e) {
            return null;
        }
        return routeJSONObject;
    }*/

    private String getRoute(String name) throws ExecutionException, InterruptedException, JSONException {
        JSONObject routeJSONObject = new APITasks(AddContactDialogFragment.this).execute("get_route", loginRepository.getLoggedInUser().getJwtToken(), name).get();
        return routeJSONObject.getJSONArray("data").getJSONObject(0).getString("route");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int width = 800;
        int height = 800;
        getDialog().getWindow().setLayout(width, height);

        View view = inflater.inflate(R.layout.add_contact_dialog_fragment, container);
        TextView nameTextView = (TextView) view.findViewById(R.id.username_input);
        Button addBtn = (Button) view.findViewById(R.id.add_contact_add_button);
        Button cancelBtn = (Button) view.findViewById(R.id.add_contact_cancel_button);
        TextView message_input = (TextView) view.findViewById(R.id.message_input);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Test some methods
                //Eingabe nehmen und ein getRoute aufrufen
                String name = nameTextView.getText().toString();
                String jwtToken = loginRepository.getLoggedInUser().getJwtToken();
                try {
                    //routeJSONObject = getRoute(name);
                    //tcpClient = new ConnectionManagerTask(AddContactDialogFragment.this, routeJSONObject.getString("route"), 6969, getContext()).execute("Now we are connected").get();
                    String route = getRoute(name);
                    //tcpClient = new ConnectionManagerTask(AddContactDialogFragment.this, route, 6969, getContext()).execute("Now we are connected").get();
                    tcpClient = new ConnectionManagerTask(AddContactDialogFragment.this, getContext()).execute(route, "6969", message_input.getText().toString()).get();

                    //tcpClient.sendMessage(message_input.getText().toString());
                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                //String code = result.getString("code");
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.username_input);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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