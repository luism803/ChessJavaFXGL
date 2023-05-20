package model;

import utils.Constantes;

import java.util.Observable;
/**
 * Class for MenuModel
 */
public class MenuModel extends Observable {
    int time;
    /**
     * Constructor for MenuModel
     */
    public MenuModel(int time) {
        this.time = time;
    }

    /**
     * Starts the game with the time selected by the user
     * @return The time selected by the user
     */
    public int enter() {
        return time;
    }

    /**
     * Increases the time by 30 seconds
     * @return True if the time is between 30 and 300 seconds
     */
    public boolean increaseTime() {
        return setTime(time + 30);
    }

    /**
     * Decreases the time by 30 seconds
     * @return True if the time is between 30 and 300 seconds
     */
    public boolean decreaseTime() {
        return setTime(time - 30);
    }

    /**
     * Setter for time
     * @param time Time selected by the user
     * @return True if the time is between 30 and 300 seconds
     */
    public boolean setTime(int time) {
        if (time >= 30 && time <= Constantes.maxTime)
            this.time = time;
        if (this.time == time) {
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Converts the time to string (mm:ss)
     * @return Time in string format (mm:ss)
     */
    public String timeToString() {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
