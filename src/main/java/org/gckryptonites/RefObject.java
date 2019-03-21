package org.gckryptonites;

/**
 * @author lifey
 * @since 2019-03-16.
 * Inspired by Gil Tene's HeapFragger
 **/
public class RefObject {
  private Object refA = null;
  private Object refB = null;
  private long[] longArray;

  RefObject(int objArrayLength) {
    if (objArrayLength > 0)
      this.longArray = new long[objArrayLength];
  }

  RefObject() {
  }

  public long[] getLongArray() {
    return longArray;
  }

  public Object getRefA() {
    return refA;
  }

  public void setRefA(Object refA) {
    this.refA = refA;
  }

  public Object getRefB() {
    return refB;
  }

  public void setRefB(Object refB) {
    this.refB = refB;
  }
}
