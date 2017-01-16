package com.example.inwon.inwontalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by inwon on 2017-01-10.
 */

public class LoginActivity extends AppCompatActivity {

    EditText id, pw, s_id, s_pw, s_name, s_pw2;
    AlertDialog.Builder dialog;
    String _id, _pw, _name, _pw2;
    CheckLogin check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.edit_id);
        pw = (EditText) findViewById(R.id.edit_pw);
        dialog = new AlertDialog.Builder(this);
    }


    //login onClick
    public void login(View v) {
        String _id = id.getText().toString();
        String _pw = pw.getText().toString();
        check = new CheckLogin(_id, _pw);
        String result = check.check_login();
        String name = check.check_name();
        if (result.equals("OK")) {
            Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
        } else if (result.equals("FAIL")) {
            id.setText("");
            pw.setText("");
            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }

    }


    //join onClick
    public void join(View v) {
        View join_view = View.inflate(LoginActivity.this, R.layout.dialog_join, null);
        dialog.setTitle("회원가입");
        dialog.setView(join_view);
        s_id = (EditText) join_view.findViewById(R.id.join_id);
        s_pw = (EditText) join_view.findViewById(R.id.join_pw);
        s_pw2 = (EditText) join_view.findViewById(R.id.join_pw2);
        s_name = (EditText) join_view.findViewById(R.id.join_name);

        dialog.setPositiveButton("join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _id = s_id.getText().toString();
                _pw = s_pw.getText().toString();
                _pw2 = s_pw2.getText().toString();
                _name = s_name.getText().toString();

                if (_pw.equals(_pw2)) {
                    new JoinTask().execute(_id, _pw, _name);
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setNegativeButton("cancel", null);
        dialog.show();
    }

    class JoinTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String pw = params[1];
            String name = params[2];
            String link = "http://1.224.44.55/join_chat.php";
            String data;
            try {

                URL url = new URL(link);
                data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pw, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                ow.write(data);
                ow.flush();
                ow.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String result = reader.readLine();
                return result;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
