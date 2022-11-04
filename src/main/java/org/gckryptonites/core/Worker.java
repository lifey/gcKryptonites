package org.gckryptonites.core;

public abstract class Worker implements Runnable{
  private final String name;
  private final String instanceId;
  protected volatile boolean doRun = true;
  private static volatile int nextInstanceId = 1;

  public void setRunningThread(Thread runningThread) {
    this.runningThread = runningThread;
  }

  Thread runningThread;

  public Worker(String name, String instanceId) {
    this.name = name;
    this.instanceId = instanceId;
    nextInstanceId++;
  }
  public Worker(String name) {
    this(name, Integer.toString(nextInstanceId));
  }

  public String getName() {
    return name;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void terminate() {
    doRun = false;
    if (runningThread != null) {
      runningThread.interrupt();
    }
  }
  protected void init() {}
  protected  void runIteration() {
    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  public void run() {
    init();
    while (doRun) {
      runIteration();
    }
  }
}
