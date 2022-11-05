package org.gckryptonites.core;

import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.config.MainConfig;
import java.util.HashMap;
import static java.util.Arrays.*;

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
    mounts.keySet().forEach(worker -> {
      worker.terminate();
      mounts.get(worker).interrupt();
    });
    mounts.keySet().forEach(worker -> {
      try {
        mounts.get(worker).join(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static void mount(MainConfig config) {
    mount(new PauseDetector(config.detectorConfig()));
    stream(config.holderConfigs())
        .map(StateHolder::new)
        .forEach(Harness::mount);
    stream(config.brutalConfigs())
        .map(BrutalAllocator::new)
        .forEach(Harness::mount);

  }
}
