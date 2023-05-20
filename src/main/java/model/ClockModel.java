package model;

import java.util.Observable;
/**
 * Class ClockModel
 * Model of the clock
 */
public class ClockModel extends Observable {
    boolean run;
    private double[] playersTimes;
    /**
     * Constructor
     * @param time Time of the clock
     */
    public ClockModel(double time) {
        playersTimes = new double[2];
        playersTimes[0] = time;
        playersTimes[1] = time;
        run = true;
    }

    /**
     * Get the time of the clock
     * @return Time
     */
    public double[] getPlayersTimes() {
        return playersTimes;
    }
    /**
     * Check if the clock is running
     * @return True if it is running
     */
    public boolean isRun() {
        return run;
    }
    /**
     * Decrease the time of the clock
     * @param tpf Time per frame
     * @param lado Side of the player
     * @return true if the time is over
     */
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
    /**
     * Convert the time to string (mm:ss:hh)
     * @param lado Side of the player
     * @return Time in string
     */
    public String timeToString(int lado) {
        double time = playersTimes[lado];
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int hundredths = (int) (((time % 60) - seconds) * 100);
        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths);
    }
    /**
     * Set the time of the clock for both players
     * @param time Time of the clock
     */
    public void setTime(int time) {
        if (run) {
            playersTimes[0] = time;
            playersTimes[1] = time;
            setChanged();
        }
    }
    /**
     * Stop the clock
     */
    private void stop() {
        run = false;
    }
}
