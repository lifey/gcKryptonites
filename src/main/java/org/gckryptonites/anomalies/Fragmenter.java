package org.gckryptonites.anomalies;

import java.io.PrintStream;
import java.util.Random;

public class Fragmenter extends Thread{
  private int arrayLen;
  private int minSize;
  private int maxSize;
  private int keepEvery;
  private Random generator = new Random();
  private final PrintStream reportStream;
  public Fragmenter(int arrayLen, int minSize, int maxSize, int KeepEvery, PrintStream reportStream) {
    this.arrayLen = arrayLen;
    this.maxSize = maxSize;
    this.minSize = minSize;
    this.keepEvery = KeepEvery;
    this.reportStream = reportStream;
    this.setDaemon(true);
    this.setName("Heap Fragmenter");
  }


  public void run() {
    Object[] fragments = new Object[arrayLen];
    for(int i = 0;i<arrayLen;i++) {
      int sz =  maxSize;
        byte[] fragment = new byte[sz];
        fragments[i] = fragment;
    }
    System.out.println("done allocating");

    for(int i = 0;i<arrayLen;i++) {
      if (i % keepEvery != 0)
      fragments[i] = null;
    }
    byte[] a= new byte[10048*1024];
    System.out.println("done nullifying");

    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
