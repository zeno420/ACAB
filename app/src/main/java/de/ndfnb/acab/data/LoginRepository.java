package de.ndfnb.acab.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository implements Parcelable {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    // private constructor : singleton access
    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        } else {
            return null;
        }
        return result;
    }

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.getLoggedInUser(), flags);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<LoginRepository> CREATOR = new Parcelable.Creator<LoginRepository>() {
        public LoginRepository createFromParcel(Parcel in) {
            return new LoginRepository(in);
        }

        public LoginRepository[] newArray(int size) {
            return new LoginRepository[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    protected LoginRepository(Parcel in) {
        this.user = (LoggedInUser) in.readParcelable(LoggedInUser.class.getClassLoader());
    }



}