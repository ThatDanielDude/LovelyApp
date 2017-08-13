package de.roserstudios.lovely.model;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.Chat.ChatArrayAdapter;
import de.roserstudios.lovely.Chat.ChatMessage;
import de.roserstudios.lovely.Com.Connection;
import de.roserstudios.lovely.Com.ServerMessage;
import de.roserstudios.lovely.Com.ServerTask;
import de.roserstudios.lovely.R;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView lvMessages;
    private EditText edtMessage;
    private Button btnSend;
    private boolean side = false;
    public List<ChatMessage> messages = new Vector<ChatMessage>();
    private Connection connection = new Connection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages.add(new ChatMessage(1, "Test1"));
        messages.add(new ChatMessage(2, "Test2"));
        messages.add(new ChatMessage(3, "Test3"));
        messages.add(new ChatMessage(4, "Test4"));

        lvMessages = (ListView) findViewById(R.id.c_LvMessageList);
        edtMessage = (EditText) findViewById(R.id.c_EdtMessage);
        btnSend = (Button) findViewById(R.id.c_BtnSend);

        chatArrayAdapter = new ChatArrayAdapter(getBaseContext(), messages);
        lvMessages.setAdapter(chatArrayAdapter);

        edtMessage.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        lvMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvMessages.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvMessages.setSelection(chatArrayAdapter.getCount() - 1);
                    }
                });

            }
        });

        connection.execute(this);
    }

    public void sending(View v){
        sendChatMessage();
    }

    private boolean sendChatMessage() {
        String msgToSend = edtMessage.getText().toString();
        if(msgToSend.equals("")) return false;

        ChatMessage msg = new ChatMessage(messages.size()+1, edtMessage.getText().toString());
        /*
        messages.add(msg);
        chatArrayAdapter.updateChat(messages);
        */
        edtMessage.setText("");
        String m = ServerTask.ADD_NEW_CHAT_MESSAGE + ";" + msg.get_id() + ";" + msg.getMsg() + ";" + msg.getTimeHHmm() + ";";
        new SendMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, m);
        return true;
    }

    public void addChatMessageFromServer(ChatMessage msg){
        System.out.println("Communication Successful");
        messages.add(msg);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatArrayAdapter.updateChat(messages);
            }
        });
    }

    private class SendMessage extends AsyncTask<String, Void, Void>{

        Socket cl;
        @Override
        protected Void doInBackground(String... strings) {


            try{

                cl = new Socket("51.254.124.10", 64444);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cl.getOutputStream()));
                print("Writing");
                writer.write(strings[0] + "\n");
                print("Writing Complete");
                writer.flush();
                print("Flushing");
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Failed to send msg");
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


    private class SendMsg extends AsyncTask<String[], Void, String[]>{

        Socket client;
        @Override
        protected String[] doInBackground(String[]... strings) {
            String[] result = null;
            String id = strings[0][0];
            String usrMsg = strings[0][1];
            String timeSent = strings[0][2];
            ServerTask srvTask = ServerTask.ADD_NEW_CHAT_MESSAGE;

            try {
                client = new Socket("51.254.124.10", 64444);

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                String messageToServer = srvTask + ";" + id + ";" + usrMsg + ";" + timeSent + ";";

                writer.write(messageToServer + "\n");
                writer.flush();


                reader.close();
                writer.close();
                client.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            Toast.makeText(getBaseContext(), "Sent...", Toast.LENGTH_SHORT).show();
        }
    }

    private static void print(String msg) {
        System.out.println("\t\t\t" + msg);
    }
}
