package org.gckryptonites.cpumeasure;

import java.util.concurrent.ConcurrentHashMap;

import static org.gckryptonites.cpumeasure.JVMThreadCPUProbe.*;

public class JVMThreadsCPUProbeMXBean implements JVMThreadsCPUProbeMBean {
  private ConcurrentHashMap<String, Long> aggrMap = new ConcurrentHashMap<String, Long>();
  public long getPausedCPU() {
    return aggrMap.getOrDefault(STW_GC_THREAD_PREFIX, 0L);
  }
  public long getConcCPU() {
    return aggrMap.getOrDefault(G1_CONC_PREFIX, 0L) + aggrMap.getOrDefault(SHENANDOAH_PREFIX, 0L)
        +aggrMap.getOrDefault(ZGC_RUNTIME_WORKER_PREFIX, 0L)+ aggrMap.getOrDefault(ZGC_WORKER, 0L)
        +aggrMap.getOrDefault(ZGC_DRIVER, 0L)+aggrMap.getOrDefault(ZGC_UNCOMMITTER, 0L)
        +aggrMap.getOrDefault(ZGC_UNMAPPER, 0L)+aggrMap.getOrDefault(ZGC_STAT, 0L);
  }
  public long getC1CPU() {
    return aggrMap.getOrDefault(C1_PREFIX, 0L);
  }
  public long getC2CPU() {
    return aggrMap.getOrDefault(C2_PREFIX, 0L);
  }
  public void setThreadGroupCPU(String key, long cpuConsumedMs) {
    aggrMap.put(key, cpuConsumedMs);
  }

  public long  getThreadGroupCPU(String key) {
    return aggrMap.getOrDefault(key,0L);
  }
}
