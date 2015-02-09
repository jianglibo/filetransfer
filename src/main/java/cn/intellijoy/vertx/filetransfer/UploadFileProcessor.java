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

  /**
   * 如果最后一次呼叫这个方法时，刚好传输完毕，那么status已经转变，接下来的代码可以
   * 执行输出返回数据到客户端？？真的吗？既然是异步写入，可能状态改变之前，下面的代码已经执行过了，
   * 那是再没有数据来驱动，程序会陷入不响应的状态。
   * 除非将socket引入到这个类！！！
   * 而且判断异步是否全部完成也存在同步问题，比如buffnum，pos等。
   * @param buffer
   */
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
