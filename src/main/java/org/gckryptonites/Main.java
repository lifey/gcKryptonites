package org.gckryptonites;

import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;
import org.gckryptonites.core.Harness;
import org.gckryptonites.core.PauseDetectorConfig;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
  static Logger logger = Logger.getLogger(Main.class.getName());
  public static void main(String[] args) throws InterruptedException {
    logger.info("Lets start");
    MainConfig config = config1();
    Harness.mount(config);

    stopAfter(120);
    //  System.out.println("Number of JVM pauses to breach 10 ms deadline is "+ detector.getCountLowPauseBreaches());
    //  System.out.println("Number of JVM pauses to breach 100 ms deadline is "+ detector.getCountAwefulPauseBreaches());
  }

  private static MainConfig config1() {
    BrutalAllocatorConfig[] bc = {new BrutalAllocatorConfig(500, 2000)};
    StateHolderConfig[] shc = {new StateHolderConfig(2000)};
    return new MainConfig(new PauseDetectorConfig(10, 10, 100),
        shc,
        bc);
  }

  private static void stopAfter(int seconds) throws InterruptedException {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < seconds * 1000) {
      Thread.sleep(1000);
    }
  }
}
