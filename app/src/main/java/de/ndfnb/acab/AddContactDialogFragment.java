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

import de.ndfnb.acab.data.LoginRepository;

public class AddContactDialogFragment extends DialogFragment {
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

            }
        });
        return view;
    }


    void addFriend(String name) {

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



}