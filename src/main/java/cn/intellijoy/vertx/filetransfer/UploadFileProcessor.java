package cn.intellijoy.vertx.filetransfer;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;

import cn.intellijoy.vertx.filetransfer.SocketStatus.SocketState;

public class UploadFileProcessor {

  private UploadHeader header;
  
  private BufferReceiver br;

  private AsyncFile fileToSave;

  private long pos = 0;

  private long buffnum = 0;

  private long completenum = 0;

  private Vertx vertx;

  public UploadFileProcessor(Vertx vertx, BufferReceiver br) {
    this.vertx = vertx;
    this.br = br;
    this.header = br.getHeader();
    initMe();
  }

  private void initMe() {
    fileToSave =
        vertx.fileSystem().openSync("testdatafolder/sync-write.dat", null, false, true, true, true);
  }

  public void appendBuffer(Buffer buffer) {
    buffnum++;
    fileToSave.write(buffer, pos, new AsyncResultHandler<Void>() {
      public void handle(AsyncResult<Void> ar) {
        if (ar.succeeded()) {
          completenum++;
        } else {
          ;
        }
      }
    });
    pos = pos + buffer.length();
    
    if (pos > header.getFileLength()) {
      br.getStatus().setState(SocketState.FILE_ERROR);
    } else if (pos == header.getFileLength()) {
      br.getStatus().setState(SocketState.UPLOAD_COMPLETE);
    }
  }
}
