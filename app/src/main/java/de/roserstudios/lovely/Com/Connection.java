package de.roserstudios.lovely.Com;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import de.roserstudios.lovely.Chat.ChatMessage;
import de.roserstudios.lovely.model.ChatActivity;

/**
 * Created by danie_000 on 11.07.2017.
 */

public class Connection extends AsyncTask<Context, Void, String> {

    static Socket client;
    public static boolean readyToReceive = true;
   // public static BufferedWriter writer;
    private ChatActivity chatActivity;

    @Override
    protected String doInBackground(Context... contexts) {
        chatActivity = (ChatActivity)contexts[0];
        ChatMessage msg;
        try {
            client = new Socket("51.254.124.10", 64444);

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
           // writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            while(readyToReceive) {
                String messageFromServer = reader.readLine();

                if(messageFromServer != null){
                    System.out.println("recieving message");
                    String[] pieces = messageFromServer.split(";");

                    if(pieces[0].equals(ServerMessage.NEW_CHAT_MESSAGE.toString())){
                        msg = new ChatMessage(Long.parseLong(pieces[1]), pieces[2]);
                        msg.setTimeHHmm(pieces[3]);
                        chatActivity.addChatMessageFromServer(msg);
                    }
                }
            }

            System.out.println("Out of loop");
            reader.close();
            client.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void sendmsg(String msg){
        print("Attempt Sending");
/*
        try{


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cl.getOutputStream()));
            print("Writing");
            writer.write(msg + "\n");
            print("Writing Complete");
            writer.flush();
            print("Flushing");

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Failed to send msg");
        }

        */
    }



    private static void print(String msg) {
        System.out.println("\t\t\t" + msg);
    }
}
