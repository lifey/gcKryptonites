package org.gckryptonites.anomalies;

import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.core.AClassWith16Bytes;
import org.gckryptonites.core.Worker;

import java.util.Random;
import java.util.logging.Logger;

public class BrutalAllocator extends Worker {
  static Logger logger = Logger.getLogger(BrutalAllocator.class.getName());

  private final Random generator = new Random(1);
  private final AClassWith16Bytes[] arrOfObjects;

  private int ptr = 0;
  private final BrutalAllocatorConfig config;

  public BrutalAllocator(BrutalAllocatorConfig config) {
    super("BrutalAllocator");
    this.config = config;

    arrOfObjects = new AClassWith16Bytes[config.arrayLen()];

  }

  short progress(short val) {
    short myVal = (short) (val + generator.nextInt());
    if (ptr >= arrOfObjects.length) {
      ptr = 0;
    }
    arrOfObjects[ptr] = new AClassWith16Bytes(myVal);
    ptr++;
    return myVal;
  }

  private int iteration;
  private int sumMs;

  @Override
  public void onInit() {
    iteration = 1;
    sumMs = 0;
  }

  @Override
  public void runIteration() {
    short val = 0;
    int total = 0;
    long start = System.currentTimeMillis();
    while (total * 16 < config.MBperSec() * 1024 * 1024) {
      for (int i = 0; i < 1000; i++) {
        val = progress(val);
        total++;
      }
    }

    long iterationTook = System.currentTimeMillis() - start;
    sumMs += iterationTook;
    logger.info("["+Thread.currentThread().getName()+ "] finished allocation of " + total + " in " + iterationTook + "ms (avg" + sumMs / iteration + " ms)");
    iteration++;
    if (iterationTook < 1000) {
      try {
        Thread.sleep(1000 - iterationTook);
      } catch (InterruptedException e) {
        logger.info("Shutting down");
      }
    }
  }
}
