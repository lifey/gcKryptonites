package org.gckryptonites;

import org.gckryptonites.anomalies.Fragmenter;
import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.FragmenterConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;
import org.gckryptonites.core.PauseDetector;
import org.gckryptonites.core.Mount;
import org.gckryptonites.core.PauseDetectorConfig;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class Main {
  public static void main(String[] args) throws InterruptedException {
    PauseDetector detector = new PauseDetector(
        new PauseDetectorConfig(10,10,100),
        System.err);
    Mount.mount(detector);
    config1();

    Mount.mount(new Fragmenter(
        new FragmenterConfig(2080,1024*1024,1024*1024,2),
        System.err));




    Mount.mount(new StateHolder(new StateHolderConfig(2000),System.err));
    var config = new BrutalAllocatorConfig(500,10000);
    Mount.mount(new BrutalAllocator(config,System.err));
    Mount.mount(new BrutalAllocator(config,System.err));

    stopAfter(120);
    System.out.println("Number of JVM pauses to breach 10 ms deadline is "+ detector.getCountLowPauseBreaches());
    System.out.println("Number of JVM pauses to breach 100 ms deadline is "+ detector.getCountAwefulPauseBreaches());
  }

  private static MainConfig config1() {
    BrutalAllocatorConfig[] bc = {new BrutalAllocatorConfig(500,2000)};
    StateHolderConfig[] shc = {new StateHolderConfig(2000)};
    return new MainConfig(new PauseDetectorConfig(10,10,100),
        shc,
        bc);
  }

  private static void stopAfter(int seconds) throws InterruptedException {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis()-start < seconds*1000) {
      Thread.sleep(1000);
    }
  }
}
