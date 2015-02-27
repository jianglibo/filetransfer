package cn.intellijoy.vertx.filetransfer.unit;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class CpTest {

  @Test
  public void t() {
    URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();

    for (URL url : urls) {
      System.out.println(url);
    }
  }
}
