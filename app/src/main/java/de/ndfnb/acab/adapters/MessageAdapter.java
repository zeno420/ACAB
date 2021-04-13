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

package de.ndfnb.acab.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.ObservableList;

import de.ndfnb.acab.ACAB;
import de.ndfnb.acab.R;
import de.ndfnb.acab.data.Message;

public class MessageAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    Message[] data;
    ObservableList<Message> list;
    ObservableList.OnListChangedCallback<ObservableList<Message>> cb;

    private static LayoutInflater inflater = null;
    String name;

    public MessageAdapter(Context context, Activity activity, String name) {
        this.context = context;
        this.activity = activity;
        this.name = name;
        this.list = ACAB.getChatListsMap().get(name);

        this.cb = new ObservableList.OnListChangedCallback<ObservableList<Message>>() {
            @Override
            public void onChanged(ObservableList<Message> sender) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
                MessageAdapter.this.activity.runOnUiThread(() -> ACAB.getContactAdapter().notifyDataSetChanged());
            }

            @Override
            public void onItemRangeChanged(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
                MessageAdapter.this.activity.runOnUiThread(() -> ACAB.getContactAdapter().notifyDataSetChanged());
            }

            @Override
            public void onItemRangeInserted(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
                MessageAdapter.this.activity.runOnUiThread(() -> ACAB.getContactAdapter().notifyDataSetChanged());
            }

            @Override
            public void onItemRangeMoved(ObservableList<Message> sender, int fromPosition, int toPosition, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
                MessageAdapter.this.activity.runOnUiThread(() -> ACAB.getContactAdapter().notifyDataSetChanged());
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
                MessageAdapter.this.activity.runOnUiThread(() -> ACAB.getContactAdapter().notifyDataSetChanged());
            }
        };

        this.list.addOnListChangedCallback(this.cb);

        this.data = list.toArray(new Message[0]);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.layout.msg_row, null);

        TextView textView = (TextView) convertView.findViewById(R.id.text);

        textView.setText(data[position].getDisplayedText());
        return convertView;
    }

    public Message[] getData() {
        return data;
    }

    public ObservableList<Message> getList() {
        return list;
    }

    public void startViewingMsg(int position, View view) {

        Message currentMsg = data[position];
        currentMsg.open();

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(currentMsg.getDisplayedText());

        Thread msgDestroyer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000 * currentMsg.getSecVisible());
                    list.remove(currentMsg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        msgDestroyer.start();
    }
}