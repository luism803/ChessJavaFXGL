package model;

import utils.Constantes;

import java.util.Observable;

public class MenuModel extends Observable {
    int time;

    public MenuModel(int time) {
        this.time = time;
    }

    public int enter() {
        return time;
    }

    public boolean increaseTime() {
        return setTime(time + 30);
    }

    public boolean decreaseTime() {
        return setTime(time - 30);
    }

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

    public String timeToString() {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
