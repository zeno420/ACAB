package de.ndfnb.acab.data;

import de.ndfnb.acab.data.model.LoggedInUser;
import de.ndfnb.acab.database.Database;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {

            Database database = new Database("h2896907.stratoserver.net", "3306", "acab_registry", username, password);
            if (database.getConnection() != null) {
                LoggedInUser user = new LoggedInUser(java.util.UUID.randomUUID().toString(), username);
                return new Result.Success<>(user);
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return null;
    }


    public void logout() {
        // TODO: revoke authentication
    }
}

mysql -u nico -p 'passwort123' -hh2896907.stratoserver.net -P 3306 -D acab_registry