package model;

import java.util.Observable;

public class MessageModel extends Observable{
    private String message;

    public MessageModel() {
        message = "";
    }

    public String getMessage() {
        System.out.println("->"+message); return message;
    }

    public void setMessage(String message) {
        this.message = message;
        setChanged();
    }
}