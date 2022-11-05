package org.gckryptonites.core;


/*
 This class is expected to consume 16 bytes on 32bit /  compressed oops 64 bit
 for ZGC it will be 24 bytes is it's oops are not compressed
 */

public class AClassWith16Bytes {
  private short internalValue;

  public AClassWith16Bytes(short value) {
    this.internalValue = value;
  }

  public short getInternalValue() {
    return internalValue;
  }

  public void setInternalValue(short internalValue) {
    this.internalValue = internalValue;
  }
}
