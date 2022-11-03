package org.gckryptonites;

import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.core.PauseDetector;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class Main {
  public static void main(String[] args) throws InterruptedException {
    PauseDetector detector = new PauseDetector(10,10,100,System.err);
    detector.start();
    //Fragmenter f = new Fragmenter(2080,1024*1024,1024*1024,2,System.err);
    //f.start();
    StateHolder bl = new StateHolder(2000,System.err);
    bl.start();
   BrutalAllocator b = new BrutalAllocator(10000,500,System.err);
    b.start();
    BrutalAllocator b2 = new BrutalAllocator(10000,500,System.err);
    b2.start();
    /*BrutalAllocator b3 = new BrutalAllocator(10000,500,System.err);
    b3.start();*/
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis()-start < 120*1000) {
      Thread.sleep(1000);
    }
    System.out.println("Number of JVM pauses to breach 10 ms deadline is "+ detector.getCountLowPauseBreaches());
    System.out.println("Number of JVM pauses to breach 100 ms deadline is "+ detector.getCountAwefulPauseBreaches());
  }
}
