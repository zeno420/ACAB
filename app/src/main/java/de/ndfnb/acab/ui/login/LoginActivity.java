/*
    ACAB (All Chats Are Beautiful)
    Copyright (C) 2021  Zeno Berkhan, Nico Diefenbacher

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package de.ndfnb.acab.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ndfnb.acab.ui.main.MainActivity;
import de.ndfnb.acab.R;
import de.ndfnb.acab.data.LoginDataSource;
import de.ndfnb.acab.data.LoginRepository;
import de.ndfnb.acab.data.LoggedInUser;

public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //execute the async task
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDataSource loginDataSource;
                LoginRepository loginRepository;
                LoggedInUser loggedInUser;
                loggedInUser = null;
                loginDataSource = new LoginDataSource();
                loginRepository = new LoginRepository(loginDataSource);

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                loginRepository.login(username, password);
                loggedInUser = loginRepository.getLoggedInUser();
                if (loggedInUser != null) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("loginRepository", loginRepository);
                    startActivity(intent);
                } else {
                    CharSequence text = "Username or password wrong!";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}