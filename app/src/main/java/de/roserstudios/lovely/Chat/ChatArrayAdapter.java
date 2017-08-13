package de.roserstudios.lovely.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import de.roserstudios.lovely.R;

/**
 * Created by danie_000 on 06.07.2017.
 */

public class ChatArrayAdapter extends BaseAdapter {

    private List<ChatMessage> chatMessageList = new Vector<ChatMessage>();
    private Context context;

    public ChatArrayAdapter(Context context, List<ChatMessage> messages) {
        super();
        this.context = context;
        chatMessageList = messages;
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return chatMessageList.get(position).get_id();
    }

    public void updateChat(List<ChatMessage> messages){
        chatMessageList = messages;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.right, parent, false);
            holder = new ViewHolder();

            holder.tvChatMsg = (TextView)convertView.findViewById(R.id.c_TvMsg);

            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();

        ChatMessage chtMsg = (ChatMessage)getItem(position);
        holder.tvChatMsg.setText(chtMsg.getMsg() + " " + chtMsg.getTimeHHmm());

        return convertView;
    }

    private class ViewHolder{
        TextView tvChatMsg;
    }
}
