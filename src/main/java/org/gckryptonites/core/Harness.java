package org.gckryptonites.core;

import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;

public class Harness {
  public static void mount(Worker r) {
    Thread t = new Thread(r);
    t.setName(r.getName()+"_" + r.getInstanceId());
    t.setDaemon(true);
    r.setRunningThread(t); //needed for interruption
    t.start();
  }

  public static void mount(MainConfig config) {
    mount(new PauseDetector(config.detectorConfig()));
    for (StateHolderConfig c : config.holderConfigs()) {
      mount(new StateHolder(c));
    }
    for (BrutalAllocatorConfig c : config.brutalConfigs()) {
      mount(new BrutalAllocator(c));
    }

  }
}
