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

package de.ndfnb.acab;

import android.app.Application;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.HashMap;
import java.util.Map;

import de.ndfnb.acab.adapters.ContactAdapter;
import de.ndfnb.acab.adapters.MessageAdapter;
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