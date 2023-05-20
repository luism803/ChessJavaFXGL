package model;

import java.util.Observable;
/**
 * Class for MessageModel
 */
public class MessageModel extends Observable {
    private String message;
    /**
     * Constructor for MessageModel
     */
    public MessageModel() {
        message = "";
    }
    /**
     * Getter for message
     * @return message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Setter for message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
        setChanged();
    }
}