package cn.intellijoy.vertx.filetransfer;

/**
 * @author jianglibo@gmail.com
 */

import org.vertx.java.core.buffer.Buffer;

import cn.intellijoy.vertx.filetransfer.SocketStatus.SocketState;

public class BufferReceiver {
  
  private Buffer buffer;
  
  private SocketStatus status;
  
  private UploadHeader header;
  
  public BufferReceiver() {
    this.buffer = new Buffer();
    this.status = new SocketStatus();
  }
  
  public void appendBuffer(Buffer buf) {
    switch (this.status.getState()) {
      case BEGIN:
        buffer.appendBuffer(buf);
        parseHeader();
        break;
      case HEAD_PARSED:
      default:
        break;
    }
    
  }
  
  public boolean headerParsed() {
    return this.status.getState() == SocketState.HEAD_PARSED;
  }


  private void parseHeader() {
    this.header = new UploadHeader(this.buffer);
    if (this.header.isSuccess()) {
      this.status.setState(SocketState.HEAD_PARSED);
    }
  }

  public SocketStatus getStatus() {
    return status;
  }

  public void setStatus(SocketStatus status) {
    this.status = status;
  }

  public UploadHeader getHeader() {
    return header;
  }

  public void setHeader(UploadHeader header) {
    this.header = header;
  }
}
