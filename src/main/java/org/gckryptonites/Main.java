package org.gckryptonites;

import org.gckryptonites.anomalies.StateHolder;
import org.gckryptonites.config.BrutalAllocatorConfig;
import org.gckryptonites.config.MainConfig;
import org.gckryptonites.config.StateHolderConfig;
import org.gckryptonites.core.Harness;
import org.gckryptonites.core.PauseDetectorConfig;
import org.gckryptonites.cpumeasure.JVMThreadCPUProbe;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import static org.gckryptonites.cpumeasure.JVMThreadCPUProbe.*;
import static org.gckryptonites.cpumeasure.JVMThreadCPUProbe.ZGC_STAT;
import static org.gckryptonites.cpumeasure.JVMThreadCPUProbe.ZGC_UNCOMMITTER;
import static org.gckryptonites.cpumeasure.JVMThreadCPUProbe.ZGC_UNMAPPER;

public class Main {
  static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) throws InterruptedException, FileNotFoundException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
    logger.info("Lets start");
    JVMThreadCPUProbe probe = new JVMThreadCPUProbe(30);
    probe.start();

 //   Path configPath = Paths.get("./config-state.yml");
//At first, construct Constructor object using Config.class root object of contents.
   // Constructor constructor = new Constructor(StateHolderConfig.class);
//Construct Yaml object with constructor object.
   // Yaml yaml = new Yaml(constructor);
//And then load by given Stream object specified of config.yml file.
   // StateHolderConfig config2 = yaml.load(new FileInputStream(configPath.toFile()));
    MainConfig config = config1();
    Harness.mount(config);
    stopAfter(100);
    Harness.shutdown();
    System.out.println(probe.getValue(G1_CONC_PREFIX) + " stw=" + probe.getValue(STW_GC_THREAD_PREFIX)  + " " + probe.getValue(SHENANDOAH_PREFIX) + " " +
        (probe.getValue(ZGC_RUNTIME_WORKER_PREFIX)+probe.getValue(ZGC_WORKER)
        +probe.getValue(ZGC_DRIVER)+probe.getValue(ZGC_UNCOMMITTER)+probe.getValue(ZGC_UNMAPPER)+probe.getValue(ZGC_STAT)));
  }

  private static MainConfig config1() {
    BrutalAllocatorConfig[] bc = {new BrutalAllocatorConfig(500, 2000)};
    StateHolderConfig[] shc = {new StateHolderConfig(2000)};
    return new MainConfig(new PauseDetectorConfig(10, 10, 100),
        shc,
        bc);
  }

  private static void stopAfter(int seconds) throws InterruptedException {
    Thread.sleep(seconds * 1000);
  }
}
