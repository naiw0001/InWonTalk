package com.example.inwon.inwontalk;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by inwon on 2017-01-10.
 */

public class CheckLogin {
    String id, pw;
    ArrayList list_id = new ArrayList();
    ArrayList list_pw = new ArrayList();
    ArrayList list_name = new ArrayList();
    int nameIdx;

    CheckLogin(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String check_login() {
        String result = null;

        class CheckTask extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                String link = "http://1.224.44.55/login_chat.php";
                StringBuilder json = new StringBuilder();

                try {

                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) break;
                        json.append(line + "\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String _result = login(json.toString());
                Log.d("asdasd", json.toString());
                return _result;
            }
        }

        try {
            result = new CheckTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;

    }

    public String login(String json) {
        String result = null;
        String member_id, member_pw, member_name;
        try {
            JSONArray ja = new JSONArray(json);
            Log.d("asdasd", String.valueOf(ja.length()));
            for (int i = 0; i < ja.length(); i++) {

                JSONObject jo = ja.getJSONObject(i);
                member_id = jo.getString("id");
                member_pw = jo.getString("password");
                member_name = jo.getString("name");

                list_id.add(member_id);
                list_pw.add(member_pw);
                list_name.add(member_name);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list_id.size(); i++) {
            if (id.equals(list_id.get(i).toString()) && pw.equals(list_pw.get(i).toString())) {
                result = "OK";
                nameIdx = i;
                check_name();
                break;
            } else {
                result = "FAIL";
            }
        }

        return result;
    }


    public String check_name() {
        String name = list_name.get(nameIdx).toString();
        return name;
    }

}
