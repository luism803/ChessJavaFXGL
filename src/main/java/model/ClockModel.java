package model;

import java.util.Observable;

public class ClockModel extends Observable {
    boolean run;
    private double[] playersTimes;

    public double[] getPlayersTimes() {
        return playersTimes;
    }

    public boolean isRun() {
        return run;
    }

    public ClockModel(double time) {
        playersTimes = new double[2];
        playersTimes[0] = time;
        playersTimes[1] = time;
        run = true;
    }

    public boolean decreaseTime(double tpf, int lado) {
        if (run) {
            playersTimes[lado] -= tpf;
            setChanged();
        }
        if (playersTimes[lado] <= 0) {
            playersTimes[lado] = 0;
            stop();
            return true;
        }
        return false;
    }

    public String timeToString(int lado) {
        double time = playersTimes[lado];
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int hundredths = (int) (((time % 60) - seconds) * 100);
        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths);
    }

    public void setTime(int time) {
        if (run) {
            playersTimes[0] = time;
            playersTimes[1] = time;
            setChanged();
        }
    }

    private void stop() {
        run = false;
    }
}
