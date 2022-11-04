package org.gckryptonites;


import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;
import org.gckryptonites.core.Harness;
import org.gckryptonites.core.PauseDetectorConfig;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class Main {
  public static void main(String[] args) throws InterruptedException {
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
