package de.ndfnb.acab;

import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.data.model.Message;
import de.ndfnb.acab.tasks.APITasks;

public class AddContactDialogFragment extends DialogFragment implements APITasks.AsyncAPIResponse {
    private EditText mEditText;
    private LoginRepository loginRepository;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int width = 800;
        int height = 900;
        getDialog().getWindow().setLayout(width, height);

        View view = inflater.inflate(R.layout.add_contact_dialog_fragment, container);
        TextView nameTextView = (TextView) view.findViewById(R.id.username_input);
        Button addBtn = (Button) view.findViewById(R.id.add_contact_add_button);
        Button cancelBtn = (Button) view.findViewById(R.id.add_contact_cancel_button);

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
                try {
                    String route = getRoute(name);
                    if (route.equals("")) {
                        Toast.makeText(getContext(), "Username not found", Toast.LENGTH_SHORT);
                    } else {
                        addFriend(name);
                    }
                } catch (ExecutionException | InterruptedException | JSONException | IOException e) {
                    e.printStackTrace();
                }


            }


        });
        return view;
    }

    private void addFriend(String name) throws IOException {
        /*File dataDirectory = Environment.getDataDirectory();
        File file = new File("data/data/de.ndfnb.acab/acab_contacts.txt");
        if (file.exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.append(name);
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();//You'll need to add proper error handling here
            }
        } else {
            file.createNewFile();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.append(name);
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();//You'll need to add proper error handling here
            }
        }*/
        ACAB.getFriendList().add(name);
        ObservableList<Message> messages = new ObservableArrayList<>();
        ACAB.getChatListsMap().put(name, messages);
        ACAB.getMessageAdaptersMap().put(name, new MessageAdapter(getContext(), getActivity(), name));
    }


    private String getRoute(String name) throws ExecutionException, InterruptedException, JSONException {
        JSONObject routeJSONObject = new APITasks(AddContactDialogFragment.this).execute("get_route", loginRepository.getLoggedInUser().getJwtToken(), name).get();
        if (routeJSONObject != null) {
            if (routeJSONObject.getString("code").equals("404")) {
                return "";
            }
            return routeJSONObject.getJSONArray("data").getJSONObject(0).getString("route");
        }
        return "";
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
}