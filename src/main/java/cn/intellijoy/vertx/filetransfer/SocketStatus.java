package cn.intellijoy.vertx.filetransfer;

public class SocketStatus {
  
  private SocketState state;
  
  public SocketStatus() {
    setState(SocketState.BEGIN);
  }
  
  public SocketState getState() {
    return state;
  }

  public void setState(SocketState state) {
    this.state = state;
  }

  public enum SocketState {
    BEGIN, HEAD_PARSED, UPLOADING_FILE, FILE_ERROR, UPLOAD_COMPLETE
  }

}
