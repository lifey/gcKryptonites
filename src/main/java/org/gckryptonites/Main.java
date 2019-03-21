package org.gckryptonites;

/**
 * @author lifey
 * @since 2019-03-16.
 **/
public class Main {
  public static void main(String[] args) throws InterruptedException {
    PauseDetector detector = new PauseDetector(500,10,System.err);
    detector.start();
    while (true) {
      Thread.sleep(10000);
    }
  }
}
