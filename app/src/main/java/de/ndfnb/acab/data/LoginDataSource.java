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

package de.ndfnb.acab.data;


import org.json.JSONObject;

import de.ndfnb.acab.interfaces.AsyncAPIResponse;
import de.ndfnb.acab.tasks.APITasks;

import java.io.IOException;


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





