package com.example.reem.worldwindow;


/**
 * Created by reem on 11/29/2017.
 */

public class Chat_Model {
    private String chatmessage;
    private boolean isSend;

    public Chat_Model(String chatmessage, boolean isSend){
        this.isSend=isSend;
        this.chatmessage=chatmessage;
    }
    public Chat_Model(){

    }

    public String getChatmessage() {
        return chatmessage;
    }

    public void setChatmessage(String chatmessage) {

        this.chatmessage = chatmessage;
    }
    public boolean isSend(){

        return isSend;
    }

    public void setSend(boolean send) {

        isSend = send;
    }
}
