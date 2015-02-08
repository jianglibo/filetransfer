package cn.intellijoy.vertx.filetransfer.unit;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.buffer.Buffer;

import cn.intellijoy.vertx.filetransfer.BufferReceiver;
import cn.intellijoy.vertx.filetransfer.SocketStatus.SocketState;
import cn.intellijoy.vertx.filetransfer.UploadHeader;

public class BufferReceiverTest {

  /**
   * bf的长度是token字符的个数 + 10
   * @throws UnsupportedEncodingException
   */
  @Test
  public void t() throws UnsupportedEncodingException {
    Buffer bf = new UploadHeader((short)1, (short)1, "abc", 1000).toBuffer();
    
    BufferReceiver brc = new BufferReceiver();
    
    brc.appendBuffer(bf.getBuffer(0, 5));
    Assert.assertTrue("Socket state should be BEGIN", (brc.getStatus().getState() == SocketState.BEGIN));
    
    brc.appendBuffer(bf.getBuffer(5, 13));
    
    Assert.assertTrue("Socket state should be HEADER_PARSED", (brc.getStatus().getState() == SocketState.HEAD_PARSED));
  }
}
