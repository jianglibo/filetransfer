package cn.intellijoy.vertx.filetransfer;

/**
 * @author jianglibo@gmail.com
 * 
 */

import java.io.UnsupportedEncodingException;

import org.vertx.java.core.buffer.Buffer;

public class UploadHeader {
  
  private short tag;
  private short cmdtype;
  
  private short tokenLength;
  private String token;
  
  private int fileLength;
  
  /**
   * 根据当前的流程设计，一个socket连接过程中，必须判断是上传信息还是上传文件。
   * 对于异步系统来说，这里存在一个问题，从接收多少长度开始解析头部，
   * 如果长度设大了，那么代码就不会得到执行。
   * 根据邮件的描述，token是可变长度，这个可变长度没有放在最后，有点不太合适。
   */
  private boolean success;

  public UploadHeader(Buffer buffer) {
    parse(buffer);
  }
  
  public UploadHeader(short tag, short cmdtype, String token, int fileLength) throws UnsupportedEncodingException {
    setTag(tag);
    setCmdtype(cmdtype);
    setToken(token);
    setTokenLength((short) token.getBytes(CharSetNames.ISO_8859_1).length);
    setFileLength(fileLength);
  }
  
  
  public Buffer toBuffer() {
    Buffer buf = new Buffer();
    buf.appendShort(tag);
    buf.appendShort(cmdtype);
    buf.appendShort(tokenLength);
    buf.appendString(token, CharSetNames.ISO_8859_1);
    buf.appendInt(fileLength);
    return buf;
  }
  
  private void parse(Buffer buffer) {
    if (buffer.length() < 6) {
      return;
    }
    int nextPos = 0;
    this.tag = buffer.getShort(nextPos);
    nextPos = 2;
    this.cmdtype = buffer.getShort(nextPos);
    nextPos = 4;
    this.tokenLength = buffer.getShort(nextPos);
    nextPos = 6;
    
    if ((nextPos + this.tokenLength + 4) < buffer.length()) {
      setSuccess(false);
    } else {
      this.token = buffer.getString(nextPos, nextPos + this.tokenLength , CharSetNames.ISO_8859_1);
      this.fileLength = buffer.getInt(nextPos + this.tokenLength);
      this.success = true;
    }
  }

  public short getTag() {
    return tag;
  }

  public void setTag(short tag) {
    this.tag = tag;
  }

  public short getCmdtype() {
    return cmdtype;
  }

  public void setCmdtype(short cmdtype) {
    this.cmdtype = cmdtype;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public short getTokenLength() {
    return tokenLength;
  }

  public void setTokenLength(short tokenLength) {
    this.tokenLength = tokenLength;
  }

  public int getFileLength() {
    return fileLength;
  }

  public void setFileLength(int fileLength) {
    this.fileLength = fileLength;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
