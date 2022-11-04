package org.gckryptonites.anomalies;

import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.core.AClassWith16Bytes;
import org.gckryptonites.core.Worker;

import java.io.PrintStream;
import java.util.Random;

public class BrutalAllocator extends Worker {


  private final Random generator = new Random(1);
  private final AClassWith16Bytes[] arrOfObjs;

  private int ptr = 0;
  private final BrutalAllocatorConfig config;

  public BrutalAllocator(BrutalAllocatorConfig config, PrintStream reportStream) {
    super("BrutalAllocator");
    this.config = config;

    arrOfObjs = new AClassWith16Bytes[config.arrayLen()];

  }

  short progress(short val) {
    short myVal = (short) (val + generator.nextInt());
    if (ptr >= arrOfObjs.length) {
      ptr = 0;
    }
    arrOfObjs[ptr] = new AClassWith16Bytes(myVal);
    ptr++;
    return myVal;
  }

  public void run() {

    int iteration = 1;
    int sumMs = 0;
    while (doRun) {
      short val = 0;
      int total = 0;
      long start = System.currentTimeMillis();
      while (total * 16 < config.MBperSec() * 1024 * 1024) {
        for (int i = 0; i < 1000; i++) {
          val = progress(val);
          total++;
        }
      }
      long iterTook = System.currentTimeMillis() - start;
      sumMs += iterTook;
      System.out.println("finished allocation of "+ total + " in " + iterTook + "ms (avg" + sumMs/iteration +" ms)" );
      iteration++;
      if (iterTook < 1000) {
        try {
          Thread.sleep(1000 - iterTook);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }
}
