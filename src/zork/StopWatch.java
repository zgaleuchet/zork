package zork;

import java.io.FileWriter;
import java.io.IOException;

public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;

    StopWatch() {
        startTime = System.currentTimeMillis();
    }

    void start() {
        startTime = System.currentTimeMillis();

    }

    void eatApple() {
        startTime = startTime - 2000;
    }

    void eatWatermelon() {
        startTime = startTime - 5000;
    }

    public void stop(String username) {
        stopTime = System.currentTimeMillis();
        System.out.println("Time: " + getElapsedTimeSecs() + " seconds.");
        try {
            // create a writer
            FileWriter writer = new FileWriter("scoreboard.txt", true);
            // write data to file
            writer.append((("\n" + getElapsedTimeSecs() + " seconds. Username: " + username)));
            // close the writer
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //elaspsed time in seconds
    public double getElapsedTimeSecs() {
        double elapsed;
        elapsed = ((double) (stopTime - startTime)) / 1000;
        return elapsed;
    }
}
