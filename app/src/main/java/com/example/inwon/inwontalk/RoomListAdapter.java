package com.example.inwon.inwontalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by inwon on 2017-01-13.
 */

public class RoomListAdapter extends BaseAdapter {

    ArrayList<RoomItem> room_list = new ArrayList<RoomItem>();

    @Override
    public int getCount() {
        return room_list.size();
    }

    @Override
    public Object getItem(int position) {
        return room_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_room,parent,false);
        }

        TextView room_num = (TextView)convertView.findViewById(R.id.room_num);
        TextView room_title = (TextView)convertView.findViewById(R.id.room_title);

        RoomItem roomItem = room_list.get(position);

        room_num.setText(String.valueOf(roomItem.getRoom_num()));
        room_title.setText(roomItem.getRoom_title());

        return convertView;
    }


    public void additem(int num, String title){
      RoomItem item = new RoomItem();
        item.setRoom_num(num);
        item.setRoom_title(title);
        room_list.add(item);

    }
}
