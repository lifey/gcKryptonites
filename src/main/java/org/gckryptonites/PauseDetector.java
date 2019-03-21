package org.gckryptonites;

import java.io.PrintStream;
import java.util.Date;

/**
 * @author lifey
 * @since 2019-03-16.
 * Inspired by Gil Tene's HeapFragger
 **/

public class PauseDetector extends Thread {
  private final long wakeupInterval;
  private final long jitterThreshold;
  private long lastSleepTime;
  private volatile boolean doRun = true;
  private final PrintStream reportStream;


  PauseDetector(long wakeupInterval, long jitterThreshold, PrintStream reportStream) {
    this.wakeupInterval = wakeupInterval;
    this.jitterThreshold = jitterThreshold;
    this.reportStream = reportStream;
    this.setDaemon(true);
    this.setName("Pause detector");
  }

  public void terminate() {
    doRun = false;
  }

  public void run() {
    while (doRun) {
      long beforeSleepTime = System.currentTimeMillis();
      try {
        Thread.sleep(wakeupInterval+5);
      } catch (InterruptedException e) {
        reportStream.println(e.toString());
      }

      long currentTime = System.currentTimeMillis();
      long jitter = currentTime - beforeSleepTime - wakeupInterval;
      if (jitter   > jitterThreshold) {
        reportStream.println(new Date() + ",Pause detected," +
            (jitter) + " ms pause");
      }
      System.out.println(jitter);

    }
  }
}
