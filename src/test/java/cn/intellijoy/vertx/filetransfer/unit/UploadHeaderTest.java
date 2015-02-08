package cn.intellijoy.vertx.filetransfer.unit;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.buffer.Buffer;

import cn.intellijoy.vertx.filetransfer.UploadHeader;

public class UploadHeaderTest {

  @Test
  public void createUploaderFromData() throws UnsupportedEncodingException {
    UploadHeader uh = new UploadHeader((short) 1, (short) 1, "abc", 1000);
    Buffer bf = uh.toBuffer();
    Assert.assertEquals("header length should be 13", 13, bf.length());
    //getBuffer 不包括end指定的位置。
    Assert.assertEquals("length should be 6", 6, bf.getBuffer(0, 6).length());
    Assert.assertEquals("length should be 7", 7, bf.getBuffer(6, 13).length());
  }
  
  @Test
  public void createUploaderFromBuffer() throws UnsupportedEncodingException {
    UploadHeader uh = new UploadHeader((short) 1, (short) 1, "abc", 1000);
    Buffer bf = uh.toBuffer();
    
    UploadHeader uh1 = new UploadHeader(bf);
    Assert.assertEquals("token should equal", "abc", uh1.getToken());
    Assert.assertEquals("fileLength should equal", 1000, uh1.getFileLength());

  }
}
