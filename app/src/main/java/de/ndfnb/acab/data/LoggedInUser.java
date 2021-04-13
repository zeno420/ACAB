package de.ndfnb.acab.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Data class that captures user information for logged in users retrieved from LoggedInUser
 */
public class LoggedInUser implements Parcelable {

    private String userId;
    private String displayName;
    private String jwtToken;
    private String route;

    public LoggedInUser(String userId, String displayName, String jwtToken) {
        this.userId = userId;
        this.displayName = displayName;
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getRoute() {
        return route;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.displayName);
        out.writeString(this.userId);
        out.writeString(this.jwtToken);
        out.writeString(this.route);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<LoggedInUser> CREATOR = new Parcelable.Creator<LoggedInUser>() {
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    protected LoggedInUser(Parcel in) {
        this.displayName = in.readString();
        this.userId = in.readString();
        this.jwtToken = in.readString();
        this.route = in.readString();
    }


}