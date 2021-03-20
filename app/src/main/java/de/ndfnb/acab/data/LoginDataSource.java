package de.ndfnb.acab.data;


import de.ndfnb.acab.LoginAsyncTask;
import de.ndfnb.acab.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource{

    String result = "";
    public Result<LoggedInUser> login(String username, String password) {
        LoggedInUser user;
        try {

            user = new LoggedInUser(java.util.UUID.randomUUID().toString(), username);


        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return new Result.Success<>(user);
    }


    public void logout() {
        // TODO: revoke authentication
    }

}





