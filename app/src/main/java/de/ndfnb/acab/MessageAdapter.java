package de.ndfnb.acab;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.ndfnb.acab.data.model.Message;

public class MessageAdapter extends BaseAdapter {

    Context context;
    Activity activity;
    Message[] data;
    ObservableList<Message> list;
    ObservableList.OnListChangedCallback<ObservableList<Message>> cb;

    private static LayoutInflater inflater = null;
    String name;

    public MessageAdapter(Context context, Activity activity, String name) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.activity = activity;
        this.name = name;
        this.list = ACAB.getChatListsMap().get(name);

        this.cb = new ObservableList.OnListChangedCallback<ObservableList<Message>>() {
            @Override
            public void onChanged(ObservableList<Message> sender) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeChanged(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Message> sender, int fromPosition, int toPosition, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Message> sender, int positionStart, int itemCount) {
                MessageAdapter.this.data = list.toArray(new Message[0]);
                MessageAdapter.this.activity.runOnUiThread(MessageAdapter.super::notifyDataSetChanged);
            }
        };

        this.list.addOnListChangedCallback(this.cb);

        this.data = list.toArray(new Message[0]);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

 /*   private Message[] getMessages() throws IOException {
       ObservableList<Message> messagesList = ACAB.getChatListsMap().get(name);
       return messagesList.toArray(new Message[0]);
    }*/

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.contact_row, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(data[position].getMessage());
        return vi;
    }
}