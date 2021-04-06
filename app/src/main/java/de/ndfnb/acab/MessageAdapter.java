package de.ndfnb.acab;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.ndfnb.acab.data.model.Message;

public class MessageAdapter extends BaseAdapter {

    Context context;
    Message[] data;
    private static LayoutInflater inflater = null;
    String name;
    public MessageAdapter(Context context, String name) {
        // TODO Auto-generated constructor stub
        this.context = context;
        try {
            this.name = name;
            this.data = getMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private Message[] getMessages() throws IOException {
       List<Message> messagesList = ACAB.getChatListsMap().get(name);
       return messagesList.toArray(new Message[0]);
    }

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
    public void notifyDataSetChanged() {
        try {
            this.data = getMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.notifyDataSetChanged();
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