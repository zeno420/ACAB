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

class ContactAdapter extends BaseAdapter {

    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    public ContactAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        try {
            this.data = getFriends();
        } catch (IOException e) {
            e.printStackTrace();
        }
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