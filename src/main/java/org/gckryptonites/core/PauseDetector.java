package org.gckryptonites.core;

import java.io.PrintStream;
import java.util.Date;

/**
 * Inspired by Gil Tene's jHiccup
 **/

public class PauseDetector extends Mount {
  private final long wakeupIntervalMs;
  private final long jitterThreshold;
  private final long awefulJitterThreshold;


  private final PrintStream reportStream;
  public volatile Long lastSleepTimeObj;
  private int countLowPauseBreaches =0;
  private int countAwefulPauseBreaches =0;


  public PauseDetector(long wakeupInterval, long jitterThreshold, long awefulJitterThreshold, PrintStream reportStream) {
    super("PauseDetector");
    this.wakeupIntervalMs = wakeupInterval;
    this.jitterThreshold = jitterThreshold;
    this.awefulJitterThreshold = awefulJitterThreshold;
    this.reportStream = reportStream;
  }




  public void run()   {
    while (doRun) {
      long beforeSleepTime = System.nanoTime();
      try {
        Thread.sleep(wakeupIntervalMs);
        lastSleepTimeObj = Long.valueOf(beforeSleepTime); // Allocate an object to make sure potential allocation stalls are measured.
      } catch (InterruptedException e) {
        throw  new RuntimeException(e);
      }

      long currentTime = System.nanoTime();
      long jitter = currentTime - beforeSleepTime - wakeupIntervalMs *1000*1000;
      if (jitter   > jitterThreshold*1000*1000) {
        countLowPauseBreaches++;
        reportStream.println("["+new Date() + "] " + countLowPauseBreaches + " Pause breach detected," +
            (jitter)/1000/1000 + " ms pause");

      }
      if (jitter   > awefulJitterThreshold*1000*1000) {
        countAwefulPauseBreaches++;
        reportStream.println("["+new Date() + "] " + countAwefulPauseBreaches + " Aweful Pause breach detected," +
            (jitter)/1000/1000 + " ms pause");

      }


    }
  }

  public int getCountLowPauseBreaches() {
    return countLowPauseBreaches;
  }
  public int getCountAwefulPauseBreaches() {
    return countAwefulPauseBreaches;
  }
}
