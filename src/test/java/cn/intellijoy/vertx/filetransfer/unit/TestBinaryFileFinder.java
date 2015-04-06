package cn.intellijoy.vertx.filetransfer.unit;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;


import org.junit.Assert;
import org.junit.Test;


import cn.intellijoy.vertx.filetransfer.BinaryFileFinder;
import cn.intellijoy.vertx.filetransfer.BinaryFileFinder.BinaryFindResult;

public class TestBinaryFileFinder {
  
  private Path path = Paths.get("fixtures", "binary.txt");
  
  private String strToFind = ":header-parsed";
  
  @Test(expected=IndexOutOfBoundsException.class)
  public void te() {
    Arrays.copyOfRange("hello".getBytes(), 100, 101);
  }
  
  @Test
  public void t() {
    
    byte[] bts = strToFind.getBytes(StandardCharsets.UTF_8);
    Assert.assertEquals("should has 14 bytes", 14, bts.length);
    
    BinaryFileFinder bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 10);
    BinaryFindResult result = bff.findFirst();
    Assert.assertNotNull(result);
    Assert.assertEquals(10, result.getExtraBytes().length);
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 0);
    result = bff.findFirst();
    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.getExtraBytes().length);
    Assert.assertTrue(result.getExtraAsString().isEmpty());
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1);
    result = bff.findFirst();
    Assert.assertNotNull(result);
    Assert.assertEquals(0, result.getExtraBytes().length);
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 20);
    result = bff.findFirst();
    Assert.assertNotNull(result);
    Assert.assertEquals(18, result.getExtraBytes().length);
    
    Matcher matcher = result.getMatcher(".*?(\\d+).*?(\\d+).*");
    Assert.assertTrue(matcher.matches());
    
    Assert.assertEquals("first match is 11", "11", matcher.group(1));
    Assert.assertEquals("second match is 18", "18", matcher.group(2));
  }
}
