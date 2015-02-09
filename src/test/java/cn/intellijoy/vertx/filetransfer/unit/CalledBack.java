package cn.intellijoy.vertx.filetransfer.unit;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
 
public class CalledBack implements CallbackInterface{
    Object result;
 
    public CalledBack() {
    }
 
    public void returnResult(Object result) {
      System.out.println("Result Received "+result);
      this.result = result;
    }
 
    public void andAction() {
        ExecutorService es = Executors.newFixedThreadPool(3);
        CallingBackWorker worker = new CallingBackWorker();
        worker.setEmployer(this);
        final Future future = es.submit( worker);
        System.out.println("... try to do something while the work is being done....");
        System.out.println("... and more ....");
        System.out.println("End work:" + new java.util.Date());
    }
 
    public static void main(String[] args) {
        new CalledBack().andAction();
        System.out.println("task submitted!");
    }
 
}