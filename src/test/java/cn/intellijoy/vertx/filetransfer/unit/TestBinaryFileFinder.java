package cn.intellijoy.vertx.filetransfer.unit;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;


import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

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
    
    assertThat(bts.length, is(14));
    
    BinaryFileFinder bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 10);
    BinaryFindResult result = bff.findFirst();
    Assert.assertNotNull(result);
    assertThat(result, is(notNullValue(BinaryFindResult.class)));
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 0);
    result = bff.findFirst();
    assertThat(result, is(notNullValue(BinaryFindResult.class)));
    
    assertThat(result.getExtraBytes().length, is(0));
    
    Assert.assertTrue(result.getExtraAsString().isEmpty());
    assertThat(result.getExtraAsString().isEmpty(), is(true));
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1);
    result = bff.findFirst();
    assertThat(result, is(notNullValue(BinaryFindResult.class)));
    assertThat(result.getExtraBytes().length, is(0));
    
    bff = new BinaryFileFinder(path,strToFind, StandardCharsets.ISO_8859_1, 20);
    result = bff.findFirst();
    assertThat(result, is(notNullValue(BinaryFindResult.class)));
    assertThat(result.getExtraBytes().length, is(18));
    
    Matcher matcher = result.getMatcher(".*?(\\d+).*?(\\d+).*");
    assertThat(matcher.matches(), is(true));
    assertThat(matcher.group(1), is("11"));
    assertThat(matcher.group(2), is("18"));
  }
}
