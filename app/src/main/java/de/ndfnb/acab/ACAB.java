package de.ndfnb.acab;

import android.app.Application;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import de.ndfnb.acab.data.model.Message;

public class ACAB extends Application {

    private static Map<String, ObservableList<Message>> chatListsMap = new HashMap<>();

    private static Map<String, MessageAdapter> messageAdaptersMap = new HashMap<>();

    private static ObservableList<String> friendList = new ObservableArrayList<>();


    public static Map<String, ObservableList<Message>> getChatListsMap() {
        return chatListsMap;
    }

    public static Map<String, MessageAdapter> getMessageAdaptersMap() {
        return messageAdaptersMap;
    }

    public static ObservableList<String> getFriendList() {
        return friendList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}