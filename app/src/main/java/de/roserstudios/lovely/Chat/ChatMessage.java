package de.roserstudios.lovely.Chat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by danie_000 on 06.07.2017.
 */

public class ChatMessage {
    private String msg;
    String timeHHmm;
    private Timestamp timestamp;
    private String pattern;
    private long _id;

    public ChatMessage(long id, String message) {
        _id = id;
        msg = message;
        timestamp = new Timestamp(System.currentTimeMillis());
        pattern = "HH:mm";
        timeHHmm = new SimpleDateFormat(pattern).format(timestamp);
    }

    public String getTimeHHmm(){
        return timeHHmm;
    }

    public void setTimeHHmm(String time){
        timeHHmm = time;
    }

    public String getMsg(){
        return msg;
    }

    public long get_id(){
        return _id;
    }
}
