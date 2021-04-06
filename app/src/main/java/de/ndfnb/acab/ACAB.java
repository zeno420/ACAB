package de.ndfnb.acab;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ndfnb.acab.data.model.Message;

public class ACAB extends Application {

    private static Map<String, List<Message>> chatListsMap = new HashMap<>();

    private static Map<String, MessageAdapter> messageAdaptersMap = new HashMap<>();

    public static Map<String, MessageAdapter> getMessageAdaptersMap() {
        return messageAdaptersMap;
    }

    public static Map<String, List<Message>> getChatListsMap() {
        return chatListsMap;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


}
