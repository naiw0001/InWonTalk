package com.example.inwon.inwontalk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by inwon on 2017-01-11.
 */

public class ListViewAdapter extends BaseAdapter {


    ArrayList<ListViewItem> list = new ArrayList<ListViewItem>();


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name_list);
        TextView text = (TextView) convertView.findViewById(R.id.text_list);
        TextView my_name = (TextView) convertView.findViewById(R.id.my_name_list);
        TextView my_text  = (TextView) convertView.findViewById(R.id.my_text_list);
        LinearLayout you = (LinearLayout)convertView.findViewById(R.id.you_Layouy);
        LinearLayout my = (LinearLayout)convertView.findViewById(R.id.my_Layouy);

        ListViewItem listViewItem = list.get(position);

        name.setText(listViewItem.getName());
        text.setText(listViewItem.getText());
        my_name.setText(listViewItem.getMy_Name());
        my_text.setText(listViewItem.getMy_Text());

        my_text.setBackground(context.getResources().getDrawable(R.drawable.my_talk));
        text.setBackground(context.getResources().getDrawable(R.drawable.you_talk));

        if(name.getText().toString().equals("") && my_name.getText().toString().equals("")){
            you.setVisibility(View.VISIBLE);
            my.setVisibility(View.VISIBLE);
            my_text.setBackground(null);
            text.setBackground(null);
        }else if(name.getText().toString().equals("")){
            you.setVisibility(View.INVISIBLE);
            my.setVisibility(View.VISIBLE);
        }else if(my_name.getText().toString().equals("")){
            you.setVisibility(View.VISIBLE);
            my.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }


    public void addItem(String name, String text, String my_name, String my_text) {
        ListViewItem item = new ListViewItem();
        item.setName(name);
        item.setText(text);
        item.setMy_Name(my_name);
        item.setMy_Text(my_text);



        list.add(item);


    }

}
