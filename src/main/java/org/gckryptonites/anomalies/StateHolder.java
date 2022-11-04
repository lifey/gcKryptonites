package org.gckryptonites.anomalies;

import org.gckryptonites.core.AClassWith16Bytes;
import org.gckryptonites.core.Mount;

import java.io.PrintStream;
import java.util.Random;

public class StateHolder extends Mount {
  private int arrayLen;

  private Random generator = new Random();
  private final PrintStream reportStream;
  private volatile AClassWith16Bytes[] arrOfObjs;
  int MBperSec;
  private int ptr = 0;

  public StateHolder(int MBOfState, PrintStream reportStream) {
    super("StateHolder "+ MBOfState +"MB");
    this.arrayLen = MBOfState * (1024*1024 /20 ) ;
    arrOfObjs = new AClassWith16Bytes[arrayLen];
    this.MBperSec = MBperSec;
    this.reportStream = reportStream;

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


    short val = 0;
    int total = 0;
    long start = System.currentTimeMillis();

    for (int i = 0; i < arrOfObjs.length; i++) {
      val = progress(val);
      total++;

    }
    long iterTook = System.currentTimeMillis() - start;
    System.out.println("state finished allocation of state " + total + " in " + iterTook + "ms");
    while (doRun) {
      try {
        Thread.sleep(1000 );
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }


  }
}
