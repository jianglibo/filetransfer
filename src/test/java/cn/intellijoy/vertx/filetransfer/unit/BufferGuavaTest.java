package cn.intellijoy.vertx.filetransfer.unit;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.buffer.Buffer;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSink;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

public class BufferGuavaTest extends NeedTestDataFolder{


  @Test
  public void t1() throws IOException {
    Buffer buff = new Buffer("hello world\n".getBytes(Charsets.ISO_8859_1));

    String curdir = System.getProperty("user.dir");

    Path tf = Paths.get(curdir, "testdatafolder", "buff_guava.data");
    
    if (java.nio.file.Files.exists(tf)) {
      java.nio.file.Files.delete(tf);
    }

    ByteSink bs = Files.asByteSink(tf.toFile(), FileWriteMode.APPEND);

    for (int i = 0; i < 10; i++) {
      bs.write(buff.getBytes());
    }
    
    CharSource cs = Files.asCharSource(tf.toFile(), Charsets.ISO_8859_1);
    
    int line = cs.readLines().size();
    
    Assert.assertEquals("file container 10 line of files.", 10, line);
    
  }
}
