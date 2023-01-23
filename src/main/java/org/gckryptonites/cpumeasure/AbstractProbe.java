package org.gckryptonites.cpumeasure;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

abstract class AbstractProbe {
  private static final Logger logger = Logger.getLogger(AbstractProbe.class.getName());
  public static final long CONTINOUS_PROBING = -1L;
  private final String name;
  private final long sleepBetweenProbes;
  private final AtomicBoolean isStarted = new AtomicBoolean(false);
  private final Thread daemonThread;
  public AbstractProbe(String name, long sleepBetweenProbes) {
    this.name = name;
    this.sleepBetweenProbes = sleepBetweenProbes;
    this.daemonThread = new Thread(this::probeLoop, name);
  }
  public void start() {
    if (isStarted.compareAndSet(false, true)) {
      daemonThread.setDaemon(true);
      daemonThread.start();
    } else {
      logger.info(name + " probe is already started");
    }
  }
  public void stop() {
    daemonThread.interrupt();
  }
  public abstract void probe();
  public void probeLoop() {
    logger.info("Probe " + name + " is starting");
    long start = System.currentTimeMillis();
    long iteration = 1;
    while (!Thread.currentThread().isInterrupted()) {
      try {
        probe();
        if (sleepBetweenProbes > 0) {
          long remainingDuration = sleepBetweenProbes - (System.currentTimeMillis() - start - sleepBetweenProbes * (iteration - 1));
          System.out.println(remainingDuration);
          if (remainingDuration > 0) {
            Thread.sleep(remainingDuration);
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      iteration++;
    }
    logger.info(name + " is shutting down ");
  }
}
