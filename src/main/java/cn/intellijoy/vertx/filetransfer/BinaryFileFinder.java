package cn.intellijoy.vertx.filetransfer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.ByteProcessor;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;

public class BinaryFileFinder {

  private File dstFile;

  private byte[] bytesToFind;

  private int endIndex;

  private Charset cs = StandardCharsets.ISO_8859_1;

  private int extraByteNum = 0;

  private byte[] extraBytes = new byte[0];

  public BinaryFileFinder(Path path, byte[] bytesToFind) {
    this.dstFile = path.toFile();
    this.bytesToFind = bytesToFind;
    this.endIndex = bytesToFind.length;
  }

  public BinaryFileFinder(Path path, String str) {
    this.dstFile = path.toFile();
    this.bytesToFind = str.getBytes(cs);
    this.endIndex = bytesToFind.length;
  }


  public BinaryFileFinder(Path path, String str, Charset cs) {
    this.dstFile = path.toFile();
    this.cs = cs;
    this.bytesToFind = str.getBytes(cs);
    this.endIndex = bytesToFind.length;
  }

  public BinaryFileFinder(Path path, String str, Charset cs, int extraByteNum) {
    this.dstFile = path.toFile();
    this.cs = cs;
    this.bytesToFind = str.getBytes(cs);
    this.endIndex = bytesToFind.length;
    this.extraByteNum = extraByteNum;
  }

  public BinaryFindResult findFirst() {
    try {
      Boolean found = Files.asByteSource(dstFile).read(new MyByteProcessor());
      if (found) {
        return new BinaryFindResult(bytesToFind, cs, extraBytes);
      }
    } catch (IOException e) {}
    return null;
  }

  private class MyByteProcessor implements ByteProcessor<Boolean> {

    private int mindex = 0;

    private boolean baseMatched;

    @Override
    public boolean processBytes(byte[] buf, int off, int len) throws IOException {
      int i = off;
      int end = off + len;

      if (baseMatched) {
        if (extraBytes.length + len >= extraByteNum) {
          extraBytes = Bytes.concat(extraBytes, Arrays.copyOfRange(buf, off, end));
          return false;
        } else {
          return true;
        }
      }

      for (; i < end; i++) {
        if (buf[i] == bytesToFind[mindex]) {
          mindex++;
          if (mindex == endIndex) {
            baseMatched = true;
            if (extraByteNum == 0) {
              return false;
            }

            int remains = end - (i + 1);

            if (remains >= extraByteNum) {
              extraBytes = Arrays.copyOfRange(buf, i + 1, i + 1 + extraByteNum);
              return false;
            }
            if ((i + 1) < end) {
              extraBytes = Arrays.copyOfRange(buf, i + 1, end);
            }
            break;
          }
        } else {
          mindex = 0;
        }
      }
      return true;
    }

    @Override
    public Boolean getResult() {
      return mindex == endIndex;
    }
  }

  public static class BinaryFindResult {
    private byte[] bytesToFind;
    private byte[] extraBytes;
    private Charset cs;


    public String getExtraAsString() {
      return new String(extraBytes, cs);
    }

    public Matcher getMatcher(String pattern) {
      Pattern ptn = Pattern.compile(pattern);
      return ptn.matcher(getExtraAsString());
    }

    public BinaryFindResult(byte[] bytesToFind) {
      super();
      this.setBytesToFind(bytesToFind);
    }

    public BinaryFindResult(byte[] bytesToFind, Charset cs, byte[] extraBytes) {
      super();
      this.setBytesToFind(bytesToFind);
      this.extraBytes = extraBytes;
      this.cs = cs;
    }

    public byte[] getExtraBytes() {
      return extraBytes;
    }

    public void setExtraBytes(byte[] extraBytes) {
      this.extraBytes = extraBytes;
    }

    public Charset getCs() {
      return cs;
    }

    public void setCs(Charset cs) {
      this.cs = cs;
    }

    public byte[] getBytesToFind() {
      return bytesToFind;
    }

    public void setBytesToFind(byte[] bytesToFind) {
      this.bytesToFind = bytesToFind;
    }
  }
}
