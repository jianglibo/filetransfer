package cn.intellijoy.vertx.filetransfer.unit;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;

public abstract class NeedTestDataFolder {
  
  @Before
  public void setup() {
    String curdir = System.getProperty("user.dir");

    Path tf = Paths.get(curdir, "testdatafolder");
    
    if (!tf.toFile().exists()) {
      tf.toFile().mkdir();
    }
  }
  
}
