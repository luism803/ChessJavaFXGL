package model;

import java.util.Observable;

public class ClockModel extends Observable {
    private double[] playersTimes;

    public double[] getPlayersTimes() {
        return playersTimes;
    }

    public ClockModel(double time) {
        playersTimes = new double[2];
        playersTimes[0] = time;
        playersTimes[1] = time;
    }

    public void decreaseTime(double tpf, int lado) {
        playersTimes[lado] -= tpf;
        setChanged();
    }

    public String timeToString(int lado) {
        double time = playersTimes[lado];
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int hundredths = (int) (((time % 60) - seconds) * 100);
        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths);
    }

    public void setTime(int time) {
        playersTimes[0] = time;
        playersTimes[1] = time;
        setChanged();
    }
}
