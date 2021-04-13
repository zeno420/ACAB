package de.ndfnb.acab;

import android.app.Application;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.HashMap;
import java.util.Map;

import de.ndfnb.acab.adapter.ContactAdapter;
import de.ndfnb.acab.adapter.MessageAdapter;
import de.ndfnb.acab.data.Message;

public class ACAB extends Application {

    private static Map<String, ObservableList<Message>> chatListsMap = new HashMap<>();

    private static Map<String, MessageAdapter> messageAdaptersMap = new HashMap<>();

    private static ObservableList<String> friendList = new ObservableArrayList<>();

    private static ContactAdapter contactAdapter;


    public static Map<String, ObservableList<Message>> getChatListsMap() {
        return chatListsMap;
    }

    public static Map<String, MessageAdapter> getMessageAdaptersMap() {
        return messageAdaptersMap;
    }

    public static ObservableList<String> getFriendList() {
        return friendList;
    }

    public static ContactAdapter getContactAdapter() {
        return contactAdapter;
    }

    public static void setContactAdapter(ContactAdapter contactAdapter) {
        ACAB.contactAdapter = contactAdapter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}