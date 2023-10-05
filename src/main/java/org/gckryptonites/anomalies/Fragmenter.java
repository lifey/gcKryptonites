package org.gckryptonites.anomalies;

import org.gckryptonites.config.FragmenterConfig;
import org.gckryptonites.core.Worker;

import java.io.PrintStream;
import java.util.Random;
import java.util.logging.Logger;

public class Fragmenter extends Worker {
  FragmenterConfig config;
  static Logger logger = Logger.getLogger(Fragmenter.class.getName());

  public Fragmenter(FragmenterConfig config) {
    super("Heap Fragmenter");
    this.config = config;
  }

  @Override
  public void onInit() {
    Object[] fragments = new Object[config.arrayLen()];
    for (int i = 0; i < config.arrayLen(); i++) {
      int sz = config.maxSize();
      byte[] fragment = new byte[sz];
      fragments[i] = fragment;
    }
    logger.info("["+Thread.currentThread().getName()+ "] done allocating");


    for (int i = 0; i < config.arrayLen(); i++) {
      if (i % config.keepEvery() != 0)
        fragments[i] = null;
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    byte[] a = new byte[10048 * 1024];
    logger.info("["+Thread.currentThread().getName()+ "] done nullifying");
  }
}
