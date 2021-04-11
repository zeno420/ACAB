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

import de.ndfnb.acab.data.model.Message;

class ContactAdapter extends BaseAdapter {

    Context context;
    Activity activity;

    String[] data;
    ObservableList<String> list;
    ObservableList.OnListChangedCallback<ObservableList<String>> cb;
    private static LayoutInflater inflater = null;

    public ContactAdapter(Context context, Activity activity) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.activity = activity;

        this.list = ACAB.getFriendList();
        this.cb = new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> sender) {
                ContactAdapter.this.data = list.toArray(new String[0]);
                ContactAdapter.this.activity.runOnUiThread(ContactAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeChanged(ObservableList<String> sender, int positionStart, int itemCount) {
                ContactAdapter.this.data = list.toArray(new String[0]);
                ContactAdapter.this.activity.runOnUiThread(ContactAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeInserted(ObservableList<String> sender, int positionStart, int itemCount) {
                ContactAdapter.this.data = list.toArray(new String[0]);
                ContactAdapter.this.activity.runOnUiThread(ContactAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> sender, int fromPosition, int toPosition, int itemCount) {
                ContactAdapter.this.data = list.toArray(new String[0]);
                ContactAdapter.this.activity.runOnUiThread(ContactAdapter.super::notifyDataSetChanged);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> sender, int positionStart, int itemCount) {
                ContactAdapter.this.data = list.toArray(new String[0]);
                ContactAdapter.this.activity.runOnUiThread(ContactAdapter.super::notifyDataSetChanged);
            }
        };

        this.list.addOnListChangedCallback(this.cb);

        this.data = list.toArray(new String[0]);

        /*try {
            this.data = getFriends();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private String[] getFriends() throws IOException {
        File dataDirectory = Environment.getDataDirectory();
        File file = new File("data/data/de.ndfnb.acab/acab_contacts.txt");
        if (file.exists()) {


            List<String> contactList = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    contactList.add(line);
                }
                if (contactList.size() == 0) {
                    return new String[]{"du hast keine Freunde"};
                }
                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }
            return contactList.toArray(new String[0]);
        } else {
            file.createNewFile();


        }
        return new String[]{"du hast keine Freunde"};
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.contact_row, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(data[position]);
        return vi;
    }
}