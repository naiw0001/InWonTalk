package com.example.inwon.inwontalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by inwon on 2017-01-13.
 */

public class RoomActivity extends AppCompatActivity {

    private RoomListAdapter adapter;
    private ListView list;
    AlertDialog.Builder dialog;
    TextView room_num;
    private int idx;
    private String title;
    ArrayList idx_array = new ArrayList();
    ArrayList title_array = new ArrayList();
    String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_room);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        dialog = new AlertDialog.Builder(this);
        adapter = new RoomListAdapter();
        list = (ListView)findViewById(R.id.room_list);

        idx = chatRoom();

        Log.d("xxxxxxxxx", String.valueOf(idx));
        Log.d("cccccccccc", String.valueOf(idx_array.size()));
        if(idx_array.size() != 0) {
            for(int i = 0 ; i < idx_array.size();i++){
                String idx_temp = idx_array.get(i).toString();
                String title_temp = title_array.get(i).toString();
                Log.d("wwwwwww",idx_temp);
                Log.d("wwwwwww",title_temp);
                adapter.additem(Integer.parseInt(idx_temp),title_temp);
            }
        }

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoomItem item = (RoomItem) parent.getItemAtPosition(position);
                int num = item.getRoom_num();
                String title = item.getRoom_title();
                Intent intent = new Intent(RoomActivity.this,MainActivity.class);
                intent.putExtra("num",num);
                intent.putExtra("title",title);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mkroom:
                dialog.setTitle("방을 생성하시겠습니까?");
                View room_view = View.inflate(RoomActivity.this,R.layout.dialog_mkroom,null);
                dialog.setView(room_view);
                room_num = (TextView)room_view.findViewById(R.id.text_idx);
                final EditText room_titme = (EditText)room_view.findViewById(R.id.edit_title);

                if(idx == 0){
                    idx = 1;
                }

                room_num.setText(String.valueOf(idx+1));
                dialog.setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title = room_titme.getText().toString();
                        adapter.additem(idx+1,title);
                        createRoom();
                        idx++;
                    }
                });
                dialog.setNegativeButton("취소",null);
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public int chatRoom(){
        int val = 0;
        class numTask extends AsyncTask<String, String, Integer> {

            @Override
            protected Integer doInBackground(String... params) {
                String link = params[0];
                StringBuilder json = new StringBuilder();
                URL url;
                int temp = 0;
                try{
                    url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

                    while(true){
                        String line = br.readLine();
                        if(line == null)break;
                        json.append(line+"\n");
                    }
                    br.close();
                }catch (Exception e){e.printStackTrace();}
                temp = room_idx(json.toString());
                return temp;

            }
        }
        try {
            val =  new numTask().execute("http://1.224.44.55/chat_list_select.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return val;
    }

    public int room_idx(String json){
        int idx = 0;
        try {
            JSONArray ja = new JSONArray(json);
            for(int i = 0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                if(jo.isNull("num")){
                    idx = 1;
//                    break;
                }else {
                    idx = jo.getInt("num");
                    title = jo.getString("title");
                    idx_array.add(idx);
                    title_array.add(title);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idx;
    }

    public void createRoom(){

        class createTask extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... params) {
                String link = params[0];
                URL url;
                String data;
                try {
                    url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    data = URLEncoder.encode("idx","UTF-8") +"="+URLEncoder.encode(String.valueOf(idx),"UTF-8");
                    data += "&"+URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8");
                    OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                    ow.write(data);
                    Log.d("zxczxc",data);
                    ow.flush();
                    ow.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line = br.readLine();
                    br.close();

                }catch (Exception e){e.printStackTrace();}

                return null;
            }
        }
        new createTask().execute("http://1.224.44.55/chat_list_insert.php");

    }

}
