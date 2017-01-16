package com.example.inwon.inwontalk;

import android.content.Context;
import android.os.Handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by inwon on 2017-01-10.
 */

public class ChatClient {

    public static final String SERVER_IP = "1.224.44.55";
    Context context;
    Handler handler;

    String msg;
    String rcvMsg;
    String name;


    ChatClient(Context context) {
        this.context = context;
    }

    public void startClient() {
        handler = new Handler();
        ConnectThread thread = new ConnectThread();
        thread.start();
    }

    public void setName(String name) {
        this.name = name;
    }


    class ConnectThread extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_IP, 9999);

                Thread sender = new Thread(new ClientSender(socket, name));
                Thread receiver = new Thread(new ClientReceiver(socket));

                sender.start();
                receiver.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ClientSender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;

        ClientSender(Socket socket, String name) {
            this.socket = socket;
            this.name = name;

            try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                if (out != null) {
                    out.writeUTF(name);
                }

                while (out != null) {
                    if (msg != null) {
                        out.writeUTF(name + ":" + msg);
                        msg = null;
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        ClientReceiver(Socket socket) {
            try {
                this.socket = socket;
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (in != null) {
                try {
                    rcvMsg = in.readUTF();
                } catch (IOException e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) context).textMsg(rcvMsg);
                    }
                });
            }
        }

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}


