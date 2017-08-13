package de.roserstudios.lovely.model;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import de.roserstudios.lovely.R;
import de.roserstudios.lovely.Com.ServerMessage;
import de.roserstudios.lovely.Com.ServerTask;

public class LoginActivity extends AppCompatActivity {


    private TextView tvTitle;
    private EditText edtName;
    private EditText edtPW;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = (TextView)findViewById(R.id.m_tvTitle);
        edtName = (EditText) findViewById(R.id.m_edtLoginName);
        edtPW = (EditText)findViewById(R.id.m_edtLoginPwd);
        btnLogin = (Button)findViewById(R.id.m_btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnLogin();
            }
        });
    }

    private void setBtnLogin(){
        String[] userInfo = {"", ""};
        userInfo[0] = edtName.getText().toString();
        userInfo[1] = edtPW.getText().toString();

        if(userInfo[0].equals(""))
            Toast.makeText(getBaseContext(), "Melda you need to type in your User-Name :)", Toast.LENGTH_SHORT).show();
        else if(userInfo[1].equals(""))
            Toast.makeText(getBaseContext(), "Almost my love. Just need your password to continue :)", Toast.LENGTH_SHORT).show();
        else
            new Login().execute(userInfo);

    }


    private class Login extends AsyncTask<String[], Void, String[]>{

        Socket client;
        @Override
        protected String[] doInBackground(String[]... strings) {

            String[] result = null;
            String usrName = strings[0][0];
            String usrPW = strings[0][1];
            ServerTask srvTask = ServerTask.VALIDATE_LOGIN;

            try {
                client = new Socket("51.254.124.10", 64444);

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                String messageToServer = srvTask + ";" + usrName + ";" + usrPW + ";";

                writer.write(messageToServer + "\n");
                writer.flush();

                String messageFromServer = reader.readLine();

                if(messageFromServer != null)
                    result = messageFromServer.split(";");

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
            ServerMessage srvMsg = ServerMessage.valueOf(strings[0]);

            if(srvMsg == ServerMessage.LOGIN_ACCEPTED){
                Toast.makeText(getBaseContext(), "You are the only Person allowed!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(getBaseContext(), "You are not welcome here! Unless you are my love Melda, then try again ;)", Toast.LENGTH_SHORT).show();
        }
    }
}
