package org.gckryptonites.anomalies;

import org.gckryptonites.config.FragmenterConfig;
import org.gckryptonites.core.Mount;

import java.io.PrintStream;
import java.util.Random;

public class Fragmenter extends Mount {

  private Random generator = new Random();
  private final PrintStream reportStream;
  FragmenterConfig config;
  public Fragmenter(FragmenterConfig config, PrintStream reportStream) {
    super("Heap Fragmenter");
    this.config = config;
    this.reportStream = reportStream;
  }


  public void run() {
    Object[] fragments = new Object[config.arrayLen()];
    for(int i = 0;i<config.arrayLen();i++) {
      int sz =  config.maxSize();
        byte[] fragment = new byte[sz];
        fragments[i] = fragment;
    }
    System.out.println("done allocating");

    for(int i = 0;i<config.arrayLen();i++) {
      if (i % config.keepEvery() != 0)
      fragments[i] = null;
    }
    byte[] a= new byte[10048*1024];
    System.out.println("done nullifying");

    while (doRun) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
