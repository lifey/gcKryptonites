package org.gckryptonites.core;

public abstract class Mount implements Runnable{
  private String name;
  private String instanceId;
  protected volatile boolean doRun = true;
  private static volatile int nextInstanceId = 1 ;

  public Mount(String name, String instanceId) {
    this.name = name;
    this.instanceId = instanceId;
    nextInstanceId++;
  }
  public Mount(String name) {
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
  }

  public static Thread mount(Mount r) {
    Thread t = new Thread(r.getName()+"_" + r.getInstanceId());
    t.setDaemon(true);
    t.start();
    return t;
  }
}
