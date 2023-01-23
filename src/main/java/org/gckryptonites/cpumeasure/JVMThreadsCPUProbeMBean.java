package org.gckryptonites.cpumeasure;

public interface JVMThreadsCPUProbeMBean {
  long getPausedCPU();
  long getConcCPU();
  long getC1CPU();
  long getC2CPU();
}
