package org.gckryptonites.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public abstract class Worker implements Runnable{
  static Logger logger = Logger.getLogger(Worker.class.getName());
  private final String name;
  private final String instanceId;
  protected volatile boolean doRun = true;
  private static final AtomicInteger nextInstanceId = new AtomicInteger(1);

  public Worker(String name, String instanceId) {
    this.name = name;
    this.instanceId = instanceId;
    nextInstanceId.incrementAndGet();
  }
  public Worker(String name) {
    this(name, Integer.toString(nextInstanceId.get()));
  }

  public String getName() {
    return name;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void terminate() {
    doRun = false;
  }
  protected void onInit() {}
  protected void onShutdown() {}
  protected  void runIteration() {
    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      logger.info("Shutting down");
    }
  }
  public void run() {
    onInit();
    while (doRun) {
      runIteration();
    }
    onShutdown();
  }
}
