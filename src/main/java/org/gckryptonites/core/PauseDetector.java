package org.gckryptonites.core;

import org.gckryptonites.anomalies.BrutalAllocator;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Inspired by Gil Tene's jHiccup
 **/

public class PauseDetector extends Worker {
  static Logger logger = Logger.getLogger(BrutalAllocator.class.getName());
  public volatile Long lastSleepTimeObj;
  private int countLowPauseBreaches = 0;
  private int countAwfulPauseBreaches = 0;
  PauseDetectorConfig config;

  public PauseDetector(PauseDetectorConfig config) {
    super("PauseDetector");
    this.config = config;
  }

  @Override
  public void runIteration() {
    long beforeSleepTime = System.nanoTime();
    try {
      Thread.sleep(config.wakeupIntervalMs());
      lastSleepTimeObj = beforeSleepTime; // Allocate an object to make sure potential allocation stalls are measured.
    } catch (InterruptedException e) {
      logger.info("sleep interrupted,probably shutdown this measurement is invalid");
      return;
    }

    long currentTime = System.nanoTime();
    long jitter = currentTime - beforeSleepTime - msToNanoSec(config.wakeupIntervalMs());
    if (jitter > msToNanoSec(config.jitterThreshold())) {
      countLowPauseBreaches++;
      logger.info("["+Thread.currentThread().getName()+ "] [" + new Date() + "] " + countLowPauseBreaches + " Pause breach detected," +
          (jitter) / 1000 / 1000 + " ms pause");

    }
    if (jitter > msToNanoSec(config.awfulJitterThreshold())) {
      countAwfulPauseBreaches++;
      logger.info("["+Thread.currentThread().getName()+ "] [" + new Date() + "] " + countAwfulPauseBreaches + " Awful Pause breach detected," +
          nanoSecToMs(jitter) + " ms pause");

    }
  }

  @Override
  protected void onShutdown() {
    logger.info("Number of JVM pauses to breach 10 ms deadline is "+ countLowPauseBreaches);
    logger.info("Number of JVM pauses to breach 100 ms deadline is "+ countAwfulPauseBreaches);
  }

  private long msToNanoSec(long ms) {
    return ms * 1000 * 1000;
  }
  private long nanoSecToMs(long ms) {
    return ms / 1000 / 1000;
  }
}
