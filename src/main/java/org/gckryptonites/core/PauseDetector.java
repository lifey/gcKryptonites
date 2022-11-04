package org.gckryptonites.core;

import java.io.PrintStream;
import java.util.Date;

/**
 * Inspired by Gil Tene's jHiccup
 **/

public class PauseDetector extends Worker {



  private final PrintStream reportStream;
  public volatile Long lastSleepTimeObj;
  private int countLowPauseBreaches =0;
  private int countAwefulPauseBreaches =0;
  PauseDetectorConfig config;


  public PauseDetector(PauseDetectorConfig config, PrintStream reportStream) {
    super("PauseDetector");
    this.config=config;

    this.reportStream = reportStream;
  }




  public void run()   {
    while (doRun) {
      long beforeSleepTime = System.nanoTime();
      try {
        Thread.sleep(config.wakeupIntervalMs());
        lastSleepTimeObj = beforeSleepTime; // Allocate an object to make sure potential allocation stalls are measured.
      } catch (InterruptedException e) {
        throw  new RuntimeException(e);
      }

      long currentTime = System.nanoTime();
      long jitter = currentTime - beforeSleepTime - config.wakeupIntervalMs() *1000*1000;
      if (jitter   > config.jitterThreshold()*1000*1000) {
        countLowPauseBreaches++;
        reportStream.println("["+new Date() + "] " + countLowPauseBreaches + " Pause breach detected," +
            (jitter)/1000/1000 + " ms pause");

      }
      if (jitter   > config.awefulJitterThreshold()*1000*1000) {
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
