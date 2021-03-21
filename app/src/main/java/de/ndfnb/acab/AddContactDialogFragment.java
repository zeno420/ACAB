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

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.tasks.APITasks.AsyncResponse;
import de.ndfnb.acab.data.LoginRepository;

public class AddContactDialogFragment extends DialogFragment implements AsyncResponse {
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
        int height = 800;
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
                String jwtToken = loginRepository.getLoggedInUser().getJwtToken();
                try {
                    JSONObject result = new APITasks(AddContactDialogFragment.this).execute("get_route", jwtToken, name).get();
                    System.out.println(result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
}