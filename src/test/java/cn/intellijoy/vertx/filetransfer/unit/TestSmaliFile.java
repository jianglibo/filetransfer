package cn.intellijoy.vertx.filetransfer.unit;

import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.collect.Lists;

import cn.intellijoy.vertx.filetransfer.SmaliFile;
import cn.intellijoy.vertx.filetransfer.SmaliFile.Invoker;

import static org.junit.Assert.*;


public class TestSmaliFile {

  @Test
  public void testConst() {
    String s1 = "const/4 v3, 0x6";
    String s2 = "const-wide/32 vAA, #+BBBBBBBB";

    Pattern ptn = SmaliFile.SmaliMethod.constPtn;

    Matcher m = ptn.matcher(s1);
    assertThat(m.matches(), is(true));
    assertThat(m.group(1), is("const/4"));
    assertThat(m.group(2), is("v3"));
    assertThat(m.group(3), is("0x6"));

    m = ptn.matcher(s2);
    assertThat(m.matches(), is(true));
    assertThat(m.group(1), is("const-wide/32"));
    assertThat(m.group(2), is("vAA"));
    assertThat(m.group(3), is("#+BBBBBBBB"));
  }

  // invoke-virtual {p0, p1, p2, v0, p3},
  // Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B
  @Test
  public void testMethodMtc() {
    String s =
        "invoke-virtual {p0, p1, p2, v0, p3}, Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B";
    Pattern ptn = SmaliFile.Invoker.invokePtn;

    Matcher m = ptn.matcher(s);

    assertThat(m.matches(), is(true));
    assertThat(m.group(1), is("invoke-virtual"));
    assertThat(m.group(2), is("p0, p1, p2, v0, p3"));
    assertThat(m.group(3), is("Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B"));
  }

  @Test
  public void testInvoker() {
    String s =
        "invoke-virtual {p0, p1, p2, v0, p3}, Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B";
    Invoker ivk = new Invoker(s).parseParams();
    List<String> expected = Lists.newArrayList("p0", "p1", "p2", "v0", "p3");
    assertThat(ivk.getRegisterNames(), is(expected));

    s =
        "invoke-static {p0, p1, p2, v0, p3}, Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B";

    ivk = new Invoker(s).parseParams();
    expected = Lists.newArrayList("p1", "p2", "v0", "p3");
    assertThat(ivk.getRegisterNames(), is(expected));
  }
}
