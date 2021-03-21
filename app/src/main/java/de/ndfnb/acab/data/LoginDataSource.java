package de.ndfnb.acab.data;


import org.json.JSONObject;

import de.ndfnb.acab.tasks.APITasks;
import de.ndfnb.acab.data.model.LoggedInUser;

import java.io.IOException;

import de.ndfnb.acab.tasks.APITasks.AsyncAPIResponse;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource implements AsyncAPIResponse {

    public Result<LoggedInUser> login(String username, String password) {
        LoggedInUser user = null;
        try {
            JSONObject result = new APITasks(LoginDataSource.this).execute("auth_signin", username, password).get();
            String code = result.getString("code");
            if (code.compareTo("200") == 0) {
                String name = result.getString("name");
                String accessToken = result.getString("accessToken");
                user = new LoggedInUser(java.util.UUID.randomUUID().toString(), name, accessToken);
            } else {
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return new Result.Success<>(user);
    }


    public void logout() {
        // TODO: revoke authentication
    }

    //this override the implemented method from AsyncResponse
    @Override
    public JSONObject processFinish(JSONObject output) {

        return output;
    }


}





