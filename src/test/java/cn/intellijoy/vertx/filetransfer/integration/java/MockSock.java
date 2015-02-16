package cn.intellijoy.vertx.filetransfer.integration.java;

import java.net.InetSocketAddress;
import java.util.List;


import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;

import com.google.common.collect.Lists;

public class MockSock implements NetSocket{
  
  private List<Buffer> buffers = Lists.newArrayList();
  
  @Override
  public NetSocket endHandler(Handler<Void> endHandler) {
    return null;
  }

  @Override
  public NetSocket dataHandler(Handler<Buffer> handler) {
    return null;
  }

  @Override
  public NetSocket pause() {
    return null;
  }

  @Override
  public NetSocket resume() {
    return null;
  }

  @Override
  public NetSocket exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public NetSocket setWriteQueueMaxSize(int maxSize) {
    return null;
  }

  @Override
  public boolean writeQueueFull() {
    return false;
  }

  @Override
  public NetSocket drainHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public String writeHandlerID() {
    return null;
  }

  /**
   * 在测试环境中，stream/write 总是呼叫这个函数，因为buf/as-buff的作用。
   */
  @Override
  public NetSocket write(Buffer data) {
    buffers.add(data);
    return this;
  }
  
  @Override
  public NetSocket write(String str) {
    return null;
  }

  @Override
  public NetSocket write(String str, String enc) {
    return null;
  }

  @Override
  public NetSocket sendFile(String filename) {
    return null;
  }

  @Override
  public NetSocket sendFile(String filename, Handler<AsyncResult<Void>> resultHandler) {
    return null;
  }

  @Override
  public InetSocketAddress remoteAddress() {
    return null;
  }

  @Override
  public InetSocketAddress localAddress() {
    return null;
  }

  @Override
  public void close() {
    
  }

  @Override
  public NetSocket closeHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public NetSocket ssl(Handler<Void> handler) {
    return null;
  }

  @Override
  public boolean isSsl() {
    return false;
  }

  public List<Buffer> getBuffers() {
    return buffers;
  }

  public void setBuffers(List<Buffer> buffers) {
    this.buffers = buffers;
  }
}
