package org.gckryptonites;

import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.core.PauseDetector;
import org.gckryptonites.core.Mount;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class Main {
  public static void main(String[] args) throws InterruptedException {
    PauseDetector detector = new PauseDetector(10,10,100,System.err);
    Mount.mount(detector);
    //Fragmenter f = new Fragmenter(2080,1024*1024,1024*1024,2,System.err);
    //f.start();

    Mount.mount(new StateHolder(2000,System.err));
    Mount.mount(new BrutalAllocator(10000,500,System.err));
    Mount.mount(new BrutalAllocator(10000,500,System.err));

    stopAfter(120);
    System.out.println("Number of JVM pauses to breach 10 ms deadline is "+ detector.getCountLowPauseBreaches());
    System.out.println("Number of JVM pauses to breach 100 ms deadline is "+ detector.getCountAwefulPauseBreaches());
  }

  private static void stopAfter(int seconds) throws InterruptedException {
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis()-start < seconds*1000) {
      Thread.sleep(1000);
    }
  }
}
