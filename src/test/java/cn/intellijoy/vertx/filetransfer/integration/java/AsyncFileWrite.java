package cn.intellijoy.vertx.filetransfer.integration.java;

/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

public class AsyncFileWrite extends TestVerticle {

  private static final Logger log = LoggerFactory.getLogger(AsyncFileWrite.class);

  @Test
  public void testCloseAsyncFile() {
    vertx.fileSystem().open("testdatafolder/async-wirte-async.dat",
        new AsyncResultHandler<AsyncFile>() {
          @Override
          public void handle(AsyncResult<AsyncFile> ar) {
            ar.result().close(new AsyncResultHandler<Void>() {

              @Override
              public void handle(AsyncResult<Void> ar) {
                Assert.assertTrue(ar.succeeded());
                log.info("f closed.");
                testComplete();
              }
            });
          }
        });
  }

  @Test
  public void syncWriteTest() {
    long fileLength = 10000L;

    long pos = 0;

    long buffnum = 0;

    long completenum = 0;

    String path = System.getProperty("user.dir");

    AsyncFile af =
        vertx.fileSystem().openSync("testdatafolder/sync-write.dat", null, false, true, true, true);

    Buffer buff = new Buffer("foooooooooooooooooooooooooooooooo\n");
    for (int i = 0; i < 5000; i++) {
      if (pos > fileLength) {
        log.info("fileLength reached.");
        try {
          af.close(new AsyncResultHandler<Void>() {
            @Override
            public void handle(AsyncResult<Void> ar) {
              if (ar.succeeded()) {
                log.info("syncWriteTest end.");
              } else {
                log.error("fail to close asyncFile");
              }
            }
          });
        } catch (Exception e) {
          log.info(e);
        }
        break;
      } else {
        af.write(buff, pos, new AsyncResultHandler<Void>() {
          public void handle(AsyncResult<Void> ar) {
            if (ar.succeeded()) {} else {
              log.error("Failed to write", ar.cause());
            }
          }
        });
        pos = pos + buff.length();
      }
    }

  }

  @Test
  public void asyncWriteTest() {
    vertx.fileSystem().open("testdatafolder/async-wirte.dat", new AsyncResultHandler<AsyncFile>() {

      private long fileLength = 10000L;

      private long pos = 0;

      public void handle(AsyncResult<AsyncFile> ar) {
        if (ar.succeeded()) {
          log.info("open file to write.");
          AsyncFile asyncFile = ar.result();
          // File open, write a buffer 5 times into a file
          Buffer buff = new Buffer("foooooooooooooooooooooooooooooooo\n");
          for (int i = 0; i < 5000; i++) {
            if (pos > fileLength) {
              log.info("fileLength reached.");
              try {
                asyncFile.close(new AsyncResultHandler<Void>() {
                  @Override
                  public void handle(AsyncResult<Void> ar) {
                    if (ar.succeeded()) {
                      log.info("asyncWriteTest end.");
                    } else {
                      log.error("fail to close asyncFile");
                    }
                  }
                });
              } catch (Exception e) {
                log.info(e);
              }
              try {
                Thread.sleep(1000);
                testComplete();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              break;
            } else {
              asyncFile.write(buff, pos, new AsyncResultHandler<Void>() {
                public void handle(AsyncResult<Void> ar) {
                  if (ar.succeeded()) {} else {
                    log.error("Failed to write", ar.cause());
                  }
                }
              });
              pos = pos + buff.length();
            }
          }
        } else {
          log.error("Failed to open file", ar.cause());
        }
      }
    });
  }
}
