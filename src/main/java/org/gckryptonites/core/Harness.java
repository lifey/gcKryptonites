package org.gckryptonites.core;

import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;

import java.util.HashMap;

public class Harness {
  private static final HashMap<Worker, Thread> mounts = new HashMap<>();

  public static void mount(Worker worker) {
    var t = new Thread(worker);
    t.setName(worker.getName() + "_" + worker.getInstanceId());
    t.setDaemon(true);
    mounts.put(worker, t);
    t.start();
  }

  public static void shutdown() {
    for (var worker : mounts.keySet()) {
      worker.terminate();
      mounts.get(worker).interrupt();
    }
    for (Worker worker : mounts.keySet()) {
      try {
        mounts.get(worker).join(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
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
