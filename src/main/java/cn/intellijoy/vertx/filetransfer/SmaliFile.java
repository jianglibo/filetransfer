package cn.intellijoy.vertx.filetransfer;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.reflect.TypeToken;

public class SmaliFile {

  private List<String> allLines;


  public static class SmaliMethod {
    
    //const/4 v3, 0x6
    //const-wide/32 vAA, #+BBBBBBBB
    public static Pattern constPtn = Pattern.compile("(const.*?)\\s+(.*?),\\s+(.*)");
    
    private int methodStartLine;

    private int invokeLine;

    private String methodTpl;

    private List<String> allLines;
    
    private Map<String, Object> localsMap;


    public SmaliMethod(String methodTpl, List<String> allLines) {
      super();
      TypeToken<String> stringTok = TypeToken.of(String.class);
      
      this.methodTpl = methodTpl;
      this.allLines = allLines;
    }

    public SmaliMethod scanMethodInvoke() {
      int totalLines = allLines.size();
      for (int i = 0; i < totalLines; i++) {
        String line = allLines.get(i).trim();
        if (line.startsWith(".method")) {
          methodStartLine = i;
        } else if (line.contains(methodTpl)) {
          invokeLine = i;
          break;
        } else if (isRegisterAction(line)) {
          
        }
      }
      return null;
    }

    private boolean isRegisterAction(String line) {
      
      return false;
    }

    public String getMethodTpl() {
      return methodTpl;
    }

    public void setMethodTpl(String methodTpl) {
      this.methodTpl = methodTpl;
    }
  }
  
  /**
   * invoke-kind {vC, vD, vE, vF, vG}, meth@BBBB
   * 6e: invoke-virtual
   * 6f: invoke-super
   * 70: invoke-direct
   * 71: invoke-static
   * 72: invoke-interface
   * A: argument word count (4 bits)
   * B: method reference index (16 bits)
   * C..G: argument registers (4 bits each)
   * Call the indicated method. The result (if any) may be stored with an appropriate move-result* variant as the immediately subsequent instruction.
   * invoke-virtual is used to invoke a normal virtual method (a method that is not private, static, or final, and is also not a constructor).
   * invoke-super is used to invoke the closest superclass's virtual method (as opposed to the one with the same method_id in the calling class). The same method restrictions hold as for invoke-virtual.
   * invoke-direct is used to invoke a non-static direct method (that is, an instance method that is by its nature non-overridable, namely either a private instance method or a constructor).
   * invoke-static is used to invoke a static method (which is always considered a direct method).
   * invoke-interface is used to invoke an interface method, that is, on an object whose concrete class isn't known, using a method_id that refers to an interface.
   * Note: These opcodes are reasonable candidates for static linking, altering the method argument to be a more direct offset (or pair thereof).
   * 
   * invoke-virtual {p0, p1, p2, v0, p3}, Loicq/wlogin_sdk/oidb/name_get_uin;->encode_request(J[B[B)[B
   * {}之间的参数个数，和方法的参数是相等的关系。当然，除了static之外，其它的都存在一个隐含的this。
   * @author admin
   *
   */
  public static class Invoker {
    
    private static Pattern invokePtn = Pattern.compile("invoke-(virtual|super|direct|static|interface)\\s+{(.*?)},\\s+(.*)");
    
    private Splitter splitter = Splitter.on(",").trimResults();
    
    private String invokeLine;
    
    public Invoker(String invokeLine) {
      this.invokeLine = invokeLine.trim();
    }
    
    
    
    public enum INVOKE_T {
      VIRTUAL, SUPER, DIRECT, STATIC, INTERFACE
    }
  }
}
