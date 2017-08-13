package de.roserstudios.lovely.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.TaskStackBuilder.SupportParentable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;

import de.roserstudios.lovely.R;

public class MenuActivity extends AppCompatActivity {

    private ImageButton btn_Chat;
    private ImageButton btn_BudgetPlanning;
    private ImageButton btn_Gallerie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_Chat = (ImageButton)findViewById(R.id.activity_menu_btn_Chat);
        btn_BudgetPlanning = (ImageButton)findViewById(R.id.activity_menu_btn_BudgetPlanning);
        btn_Gallerie = (ImageButton)findViewById(R.id.activity_menu_btn_Gallerie);

        btn_Chat.setOnClickListener(onClickListener);
        btn_BudgetPlanning.setOnClickListener(onClickListener);
        btn_Gallerie.setOnClickListener(onClickListener);




    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.activity_menu_btn_Chat:
                    NotifyClient();
                    break;
                case R.id.activity_menu_btn_BudgetPlanning:
                    break;
                case R.id.activity_menu_btn_Gallerie:
                    break;
            }
        }
    };

    private NotificationCompat.Builder builder =
            new NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("My Notification")
            .setContentText("The information to My Notification");

    private void NotifyClient(){
        Intent resultIntent = new Intent(getBaseContext(), ChatActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());

        stackBuilder.addParentStack(ChatActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, builder.build());
    }

}
