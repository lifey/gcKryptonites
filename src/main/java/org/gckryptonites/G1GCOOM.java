package org.gckryptonites;

import org.gckryptonites.anomalies.Fragmenter;
import org.gckryptonites.config.FragmenterConfig;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.FileNotFoundException;
import java.util.logging.Logger;


// This should throw on OOM memory on Java < 21 though half of the heap is empty
public class G1GCOOM {
  static Logger logger = Logger.getLogger(G1GCOOM.class.getName());

  public static void main(String[] args) throws InterruptedException, FileNotFoundException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

    Fragmenter fragmenter = config1();
    fragmenter.onInit();


  }

  private static Fragmenter config1() {

    FragmenterConfig fc = new FragmenterConfig(1022,4000000,2);
    return new Fragmenter(fc);
  }

  private static void stopAfter(int seconds) throws InterruptedException {
    Thread.sleep(seconds * 1000);
  }
}
