package cn.intellijoy.vertx.filetransfer.unit;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import org.junit.Assert;
import org.junit.Test;

public class MyAtomTest {

  @Test
  public void tAtomicReference() {
    Date d1 = new Date(), d2 = new Date(), d3 = new Date();
    
    AtomicReference<Date> ad = new AtomicReference<Date>(d1);
    boolean c1 = ad.compareAndSet(d1, d2);
    boolean c2 = ad.compareAndSet(d2, d3);
    Assert.assertTrue("get and set with origin ref c1", c1);
    Assert.assertTrue("get and set with origin ref c2", c2);
    Assert.assertSame(d3, ad.get());
    
    boolean f1 = ad.compareAndSet(d1, d2);
    Assert.assertFalse("get and set with wrong ref f1", f1);
    Assert.assertSame("still hold d3", d3, ad.get());
  }
  
  @Test
  public void tAtomicStampedReference() {
    AnOb a = new AnOb("a");
    AnOb b = new AnOb("b");
    AnOb c = new AnOb("c");
    
    AtomicStampedReference<AnOb> ad = new AtomicStampedReference<AnOb>(a, 0);
    boolean c1 = ad.compareAndSet(a, b,0, 1);
    boolean c2 = ad.compareAndSet(b, c, 1, 2);
    Assert.assertTrue("get and set with origin ref c1", c1);
    Assert.assertTrue("get and set with origin ref c2", c2);
    Assert.assertSame(c, ad.getReference());
    
    int[] holder = new int[1];
    AnOb ddd = ad.get(holder);
    
    Assert.assertEquals(2, holder[0]);
    Assert.assertSame(c, ddd);
    
    boolean f1 = ad.compareAndSet(a, b, 0, 1);
    Assert.assertFalse("get and set with wrong ref f1", f1);
    Assert.assertSame("still hold d3", c, ad.getReference());
  }
  
  public static class AnOb {
    private String name;
    
    public AnOb(String name) {
      this.setName(name);
    }
    
    @Override
    public String toString() {
      return name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
    
    
  }
}
