package com.example.inwon.inwontalk;

/**
 * Created by inwon on 2017-01-13.
 */

public class RoomItem {

    private String room_title;
    private int room_num;


    public void setRoom_num(int room_num){
        this.room_num = room_num;
    }

    public void setRoom_title(String room_title){
        this.room_title = room_title;

    }

    public int getRoom_num(){

        return room_num;
    }
    public String getRoom_title(){
        return room_title;
    }

}
