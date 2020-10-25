package com.example.reem.worldwindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by reem on 11/30/2017.
 */

public class MyAdabter extends BaseAdapter {
    private List<Chat_Model> chatModel;
    private Context context;
    private LayoutInflater layoutInflater;


    public MyAdabter(List<Chat_Model> chatModel, Context context){
        this.context=context;
        this.chatModel = chatModel;

        layoutInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //error at this line

    }

    @Override
    public int getCount() {
        return chatModel.size();
    }

    @Override
    public Object getItem(int position) {
        return chatModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//      View v = convertView;
        View v;
//             if( v == null ){
        if((chatModel.get(position)).isSend())
            v=layoutInflater.inflate(R.layout.send_message,null);
        else
            v=layoutInflater.inflate(R.layout.recieve_message,null);
        BubbleTextView text=(BubbleTextView)v.findViewById(R.id.message);
        text.setText(chatModel.get(position).getChatmessage());
//        }
        return v;
    }

}