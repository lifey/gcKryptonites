package org.gckryptonites.anomalies;

import org.gckryptonites.config.StateHolderConfig;
import org.gckryptonites.core.AClassWith16Bytes;
import org.gckryptonites.core.Worker;

import java.io.PrintStream;
import java.util.Random;
import java.util.logging.Logger;

public class StateHolder extends Worker {
  private final Random generator = new Random();
  private final AClassWith16Bytes[] arrOfObjs;
  static Logger logger = Logger.getLogger(BrutalAllocator.class.getName());

  private StateHolderConfig config;
  private int ptr = 0;

  public StateHolder(StateHolderConfig config) {
    super("StateHolder " + config.MBOfState() + "MB");
    int arrayLen = config.MBOfState() * (1024 * 1024 / 20);
    arrOfObjs = new AClassWith16Bytes[arrayLen];

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

  public void init() {
    short val = 0;
    int total = 0;
    long start = System.currentTimeMillis();

    for (int i = 0; i < arrOfObjs.length; i++) {
      val = progress(val);
      total++;
    }
    long elapsedTime = System.currentTimeMillis() - start;
    logger.info("["+Thread.currentThread().getName()+ "] state finished allocation of state " + total + " in " + elapsedTime + "ms");
  }
}
