package org.gckryptonites.anomalies;

import org.gckryptonites.config.FragmenterConfig;
import org.gckryptonites.core.Worker;

import java.io.PrintStream;
import java.util.Random;
import java.util.logging.Logger;

public class Fragmenter extends Worker {
  FragmenterConfig config;
  static Logger logger = Logger.getLogger(Fragmenter.class.getName());

  public Fragmenter(FragmenterConfig config, PrintStream reportStream) {
    super("Heap Fragmenter");
    this.config = config;
  }


  public void init() {
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
    byte[] a = new byte[10048 * 1024];
    logger.info("["+Thread.currentThread().getName()+ "] done nullifying");
  }
}
