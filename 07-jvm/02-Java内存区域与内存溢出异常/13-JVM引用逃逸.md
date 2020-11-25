# 引用逃逸

Java 分配在堆上的对象都是靠引用来操作的 , 当对象在某个方法中都定义之后, 把它的引用作为其他方法的参数传递过去, 这样就叫做对象的引用逃逸,而如果原本对象在当前方法结束后就会被垃圾回收器标记和回收,但由于其引用被传递出去,当被一个长期存活的对象所持有时 , 那原来的对象的生命周期就变成跟这个长期存在于堆内存的对象一样, 对于这样一些临时对象没有做到即时的回收,就会造成JVM的内存占用, 严重情况更会是触发Full GC,从而影响程序性能.

#### 臭名昭著的 this 引用逃逸

**this 引用逃逸**  在构造函数返回之前, 其他线程就通过this引用访问到了"未完成初始化"的对象, 而调用尚未构造完全的对象就会不可预知的问题, 因此this 引用逃逸引发的问题是线程安全问题.

主要发生场景是在构造函数启动线程,或者注册监听时发生,如下代码:

```java
public class UnsafeThisEscape {
   private String id;
   
   public ThisEscape(String id) {
       new Thread(new EscapeRunnable()).start();
       // ...其他代码
       this.id = id;
   }
   
   private class EscapeRunnable implements Runnable {
       @Override
       public void run() {
           System.out.println("id: "+UnsafeThisEscape.this.id);  
           // 在这里通过UnsafeThisEscape.this就可以引用UnsafeThisEscape对象, 但是此时UnsafeThisEscape对象可能还没有构造完成, 即发生了this引用的逃逸.
       }
   }
}
```

#### 如何避免 this 引用逃逸

想要避免 this 引用逃逸,那当然就是**不要在构造器中执行其他线程与当前引用对象相关的操作**, **构造器仅用来完成初始化操作**, 在上面的场景中处理方式就是在构造函数中创建线程，但不启动它。在构造函数外面再启动,可以专门提供一个方法出来,调整后如下所示

```java
public class SafeThisEscape {
   private Thread t;
   private String id;

   public ThisEscape(String id) {
       t = new Thread(new EscapeRunnable());
       this.id = id;
       // ...其他代码
   }
   
   public void init() {
       t.start();
   }  
   private class EscapeRunnable implements Runnable {
       @Override
       public void run() {
           System.out.println("id: "+UnsafeThisEscape.this.id);  
           // 这里通过SafeThisEscape.this引用的对象,是已经构造完成的,保证了线程安全.
       }
   }
}
```

还有一种逃逸场景就是针对构造器的监听注册情况,同理让监听事件的注册不在构造器中进行,而是提供一个单独方法完成.

总之,每当在一个构造器中需要做复杂逻辑处理和初始化,就应该考虑到这个问题, 应该尽量让构造器执行简单,必要的初始化操作,更多复杂处理放在单独一个方法执行.

