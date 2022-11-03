package org.gckryptonites;

import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.anomalies.BrutalAllocator;
import org.gckryptonites.core.PauseDetector;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class StateWIthHighAllocation {
  public static void main(String[] args) throws InterruptedException {
    PauseDetector detector = new PauseDetector(500,10,100,System.err);
    detector.start();
    //Fragmenter f = new Fragmenter(2080,1024*1024,1024*1024,2,System.err);
    //f.start();
    StateHolder bl = new StateHolder(10000,System.err);
    bl.start();
    BrutalAllocator b = new BrutalAllocator(10000,500,System.err);
    b.start();
    BrutalAllocator b2 = new BrutalAllocator(10000,500,System.err);
    b2.start();
    while (true) {
      Thread.sleep(10000);
    }
  }
}
