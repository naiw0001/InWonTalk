package com.example.inwon.inwontalk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    ChatClient chat;
    EditText text;
    ListView listView;
    ListViewAdapter adapter;
    LinearLayout linear;
    private String name,title;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText) findViewById(R.id.edit);
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        title = intent.getStringExtra("title");
        num = intent.getIntExtra("num",0);




        db_select();

        chat = new ChatClient(this);
        chat.setName(name);
        chat.startClient();

        linear = (LinearLayout) findViewById(R.id.activity_main);
        linear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInputWindow(text);
                return true;
            }
        });

        adapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.chatlist);
        listView.setAdapter(adapter);
    }

    public void sendText(View v) {
        String msg = text.getText().toString();
        if (text.length() == 0) {
            Toast.makeText(getApplicationContext(), "입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            chat.setMsg(msg);
            text.setText("");
        }
    }

    public void textMsg(String msg) {

        int idx = msg.indexOf(":");
        String _name = msg.substring(0, idx);
        String text = msg.substring(idx + 1, msg.length());

        if (!text.equals("접속") || !text.equals("나감")) {
        db_insert(_name,text);
        }

        if (text.equals("접속") || text.equals("나감")) {
            adapter.addItem("", _name+"님", "", text);
        }else if (name.equals(_name)) {
            adapter.addItem("", "", _name, text);
        } else {
            adapter.addItem(_name, text, "", "");
        }
        adapter.notifyDataSetChanged();

    }

    public void hideSoftInputWindow(View editView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(editView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void db_select(){
        class Select extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... params) {
                String link = params[0];
                URL url;
                StringBuilder json = new StringBuilder();
                try{
                    url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoOutput(true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    while(true){
                        String line = br.readLine();
                        if(line == null)break;
                        json.append(line+"\n");
                    }


                }catch (Exception e){e.printStackTrace();}
                db_select_json(json.toString());
                return null;
            }
        }

        new Select().execute("http://1.224.44.55/chat_list_s.php");
    }


    public void db_select_json(String json){
        try {
            JSONArray ja = new JSONArray(json);
            for(int i = 0; i < ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                String name = jo.getString("name");
                String msg = jo.getString("msg");
                if(name.equals(this.name)){
                adapter.addItem("","",name,msg);
                }else adapter.addItem(name,msg,"","");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void db_insert(String name,String text){
        class Insert extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... params) {
                String link = params[0];
                String name = params[1];
                String msg = params[2];
                URL url;
                String data;
                try{
                    url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");
                    data += "&"+ URLEncoder.encode("msg","UTF-8")+"="+URLEncoder.encode(msg,"UTF-8");
                    OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                    ow.write(data);
                    ow.flush();
                    ow.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = reader.readLine();
                    return line;
                }catch (Exception e){e.printStackTrace();}

                return null;
            }
        }

        new Insert().execute("http://1.224.44.55/chat_list.php",name,text);
    }

}
