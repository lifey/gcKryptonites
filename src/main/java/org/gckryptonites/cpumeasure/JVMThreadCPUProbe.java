package org.gckryptonites.cpumeasure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

public class JVMThreadCPUProbe extends AbstractProbe {
  public static final String G1_CONC_PREFIX = "\"G1 ";
  public static final String STW_GC_THREAD_PREFIX = "\"GC Thread"; // pausing thread name prefix works for G1 and ParallelGC
  public static final String C1_PREFIX = "\"C1 CompilerThread";
  public static final String C2_PREFIX = "\"C2 CompilerThread";
  public static final String SHENANDOAH_PREFIX = "\"Shenandoah GC Thread";
  public static final String ZGC_RUNTIME_WORKER_PREFIX = "\"RuntimeWorker";
  public static final String ZGC_WORKER = "\"ZWorker";
  public static final String ZGC_DRIVER = "\"ZDriver";
  public static final String ZGC_UNCOMMITTER = "\"ZUncommitter";
  public static final String ZGC_UNMAPPER = "\"ZUnmapper";
  public static final String ZGC_STAT = "\"ZStat";


  private static final Pattern pattern = Pattern.compile("cpu=([\\d.]+)ms");
  private final JVMThreadsCPUProbeMXBean mxBean = new JVMThreadsCPUProbeMXBean();

  public JVMThreadCPUProbe(long sleepBetweenProbes) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    super("JVMThreadsCPUProbe", sleepBetweenProbes);
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = new ObjectName("com.ni:type=G1ThreadCpuProbeMBean");
    mbs.registerMBean(new StandardMBean(mxBean, JVMThreadsCPUProbeMBean.class), name);
  }

  @Override
  public void probe() {
    long pid = ProcessHandle.current().pid();

    Process handle = null;
    try {
      handle = Runtime.getRuntime().exec("jstack " + pid);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    getSum(handle.getInputStream());
  }

  String[] keys = {G1_CONC_PREFIX, STW_GC_THREAD_PREFIX, C1_PREFIX, C2_PREFIX, SHENANDOAH_PREFIX,ZGC_WORKER,ZGC_DRIVER,ZGC_UNCOMMITTER,ZGC_UNMAPPER,ZGC_STAT,ZGC_RUNTIME_WORKER_PREFIX};

  public void getSum(InputStream inputStream) {
    HashMap<String, Double> sumMap = new HashMap<String, Double>();
    (new BufferedReader(new InputStreamReader(inputStream))).lines().forEach(it -> {
      Arrays.stream(keys).forEach(key -> {
        if (it.startsWith(key)) {
          java.util.regex.Matcher matchResult = pattern.matcher(it);
          if (matchResult.find()) {
            double cpuConsumedMs = Double.parseDouble(matchResult.group(1));
            sumMap.put(key, sumMap.getOrDefault(key, 0.0) + cpuConsumedMs);
          }
        }
      });
    });
    for (String key : keys) {
      mxBean.setThreadGroupCPU(key, sumMap.getOrDefault(key, 0.0).longValue());
    }
  }

  public long getValue(String key) {
    return mxBean.getThreadGroupCPU(key);
  }
}
