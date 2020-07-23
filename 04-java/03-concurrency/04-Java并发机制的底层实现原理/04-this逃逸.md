# This逃逸

## **什么是this逃逸**

并发编程实践中，this引用逃逸（"this"escape）是在构造器构造还未彻底完成前（即实例初始化阶段还未完成），将自身this引用向外抛出并被其他线程复制（访问）了该引用，可能会问到该还未被初始化的变量，甚至可能会造成更大严重的问题（如危及到线程安全）。

因为其他线程有可能通过这个逸出的引用访问到“初始化了一半”的对象 (partially-constructed object)。这样就会出现某些线程中看到该对象的状态是没初始化完的状态，而在另外一些线程看到的却是已经初始化完的状态，

这种不一致性是不确定的，程序也会因此而产生一些无法预知的并发错误。

## **代码示例**

```java
public class ThisEscape {
    //final常量会保证在构造器内完成初始化（但是仅限于未发生this逃逸的情况下，具体可以看多线程对final保证可见性的实现）
    final int i;
    //尽管实例变量有初始值，但是还实例化完成
    int j = 0;
    static ThisEscape obj;
    public ThisEscape() {
        i=1;
        j=1;
        //将this逃逸抛出给线程B
        obj = new ThisEscape();
    }
    public static void main(String[] args) {
        //线程A：模拟构造器中this逃逸,将未构造完全对象引用抛出
        /*Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                //obj = new ThisEscape();
            }
        });*/
        //线程B：读取对象引用，访问i/j变量
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                //可能会发生初始化失败的情况解释：实例变量i的初始化被重排序到构造器外，此时1还未被初始化
                ThisEscape objB = obj;
                try {
                    System.out.println(objB.j);
                } catch (NullPointerException e) {
                    System.out.println("发生空指针错误：普通变量j未被初始化");
                }
                try {
                    System.out.println(objB.i);
                } catch (NullPointerException e) {
                    System.out.println("发生空指针错误：final变量i未被初始化");
                }
            }
        });
            //threadA.start();
            threadB.start();
    }
}
```

输出结果：

```shell
发生空指针错误：普通变量j未被初始化
发生空指针错误：final变量i未被初始化
```

这说明ThisEscape还未完成实例化，构造还未彻底结束。

#### 示例2：

```java
public class ThisEscape {  
      public final int id;  
      public final String name;  
      public ThisEscape(EventSource<EventListener> source) {  
            id = 1;  
            source.registerListener(new EventListener() {  
                  //内部类是可以直接访问外部类的成员变量的（外部类引用this被内部类获取了）
                  public void onEvent(Object obj) {  
                        System.out.println("id: "+ThisEscape.this.id);  
                        System.out.println("name: "+ThisEscape.this.name);  
                  }  
            });  
            name = "flysqrlboy";               
      } }
```

ThisEscape 在构造函数中引入了一个内部类 EventListener，而内部类会自动的持有其外部类（这里是 ThisEscape）的 this 引用。

**source.registerListener** 会将内部类发布出去，从而 **ThisEscape.this** 引用也随着内部类被发布了出去。

但此时 ThisEscape 对象还没有构造完成，id已被赋值为1，但 name 还没被赋值，仍然为 null。

简单来说，就是在一个类的构造器创建了一个内部类（内部类本身是拥有对外部类的所有成员的访问权的），此时外部类的成员变量还没初始化完成。但是，同时这个内部类被其他线程获取到，并且调用了内部类可以访问到外部类还没来得及初始化的成员变量的方法。

#### 示例3：

```java
public class EventSource<T> {  
      private final List<T> eventListeners ;  
      public EventSource() {  
            eventListeners = new ArrayList<T>() ;  
      }  

      public synchronized void registerListener(T eventListener) {  //数组持有传入对象的引用
            this.eventListeners.add(eventListener);  
            this.notifyAll();  
      }  

      public synchronized List<T> retrieveListeners() throws InterruptedException {  //获取持有对象引用的数组
            List<T> dest = null;  
            if(eventListeners.size() <= 0 ) {  
                  this.wait();  
            }  
            dest = new ArrayList<T>(eventListeners.size());  //这里为什么要创建新数组，好处在哪里
            dest.addAll(eventListeners);  
            return dest;  
      }  
  }
```

把内部类对象发布出去的 source.registerListener 语句没什么特殊的（发布其实就是让别的类有机会持有这个内部类的引用），registerListener方法只是往list中添加一个EventListener元素而已。这样，其他持有 EventSource 对象的线程从而持有EventListener对象，便可以访问 ThisEscape 的内部状态了（id和name）。

#### 示例4：

```java
public class ListenerRunnable implements Runnable {  

      private EventSource<EventListener> source;  
      public ListenerRunnable(EventSource<EventListener> source) {  
            this.source = source;  
      }  
      public void run() {  
            List<EventListener> listeners = null;  

            try {  
                  listeners = this.source.retrieveListeners();  
            } catch (InterruptedException e) {  
                  // TODO Auto-generated catch block  
                  e.printStackTrace();  
            }  
            for(EventListener listener : listeners) {  
                  listener.onEvent(new Object());  //执行内部类获取外部类的成员变量的方法
            }  
      }  
  }
```

只要线程得到持有内部类引用的数组，就可以使用内部类获取外部类的有可能未初始化的成员变量。

#### 示例5：

```java
public class ThisEscapeTest {  

      public static void main(String[] args) {  
            EventSource<EventListener> source = new EventSource<EventListener>();  
            ListenerRunnable listRun = new ListenerRunnable(source);  
            Thread thread = new Thread(listRun);  
            thread.start();  
            ThisEscape escape1 = new ThisEscape(source);  
      }  
}
```

启动了一个ListenerRunnable 线程，用于监视ThisEscape的内部状态。主线程紧接着调用ThisEscape的构造函数，新建一个ThisEscape对象。

在ThisEscape构造函数中，如果在source.registerListener语句之后，name="flysqrlboy"赋值语句之前正好发生上下文切换，ListenerRunnable 线程就有可能看到了还没初始化完的ThisEscape对象，即id为1，但是name仍然为null。

#### 示例6：

另外一种就是在构造函数中启动新的线程的时候，容易发生This逃逸。

```java
public class ThreadThisEscape {    
    //成员变量xxx
    public ThisEscape() {    
        new Thread(new EscapeRunnable()).start();    //使用未初始化的成员变量
        // 初始化成员变量    
    }    

    private class EscapeRunnable implements Runnable {    
        @Override    
        public void run() {    
            //使用成员变量
            // ThreadThisEscape.this就可以引用外围类对象, 但是此时外围类对象可能还没有构造完成, 即发生了外围类的this引用的逃逸    
        }    
    }    
}
```

#### 示例7：

```java
public class ThisEscape {
    //final常量会保证在构造器内完成初始化（但是仅限于未发送this逃逸的情况下）
    final int i;
    //尽管实例变量有初始值，但是还实例化完成
    int j = 0;
    static ThisEscape obj;
    public ThisEscape() {
        i=1;
        j=1;
        //obj = new ThisEscape();
    }
    public static void main(String[] args) {
        //线程A：模拟构造器中this逃逸,将未构造完全对象引用抛出
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                //构造初始化中...线程B可能获取到还未被初始化完成的变量
                //类似于this逃逸，但并不定发生
                obj = new ThisEscape();
            }
        });
        //线程B：读取对象引用，访问i/j变量
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                //可能会发生初始化失败的情况解释：实例变量i的初始化被重排序到构造器外，此时1还未被初始化
                ThisEscape objB = obj;
                try {
                    System.out.println(objB.j);
                } catch (NullPointerException e) {
                    System.out.println("发生空指针错误：普通变量j未被初始化");
                }
                try {
                    System.out.println(objB.i);
                } catch (NullPointerException e) {
                    System.out.println("发生空指针错误：final变量i未被初始化");
                }
            }
        });
            threadA.start();
            threadB.start();
    }
}
```

利用线程A模拟this逃逸，但不一定会发生，线程A模拟构造器正在构造...而线程B尝试访问变量，这是因为



**（1）由于JVM的指令重排序存在，实例变量i的初始化被安排到构造器外（final可见性保证是final变量规定在构造器中完成的）；**

**（2）类似于this逃逸，线程A中构造器构造还未完全完成。**

## **如何避免**

因此，什么情况下会this逃逸？

（1）在构造器中很明显地抛出this引用提供其他线程使用（如上述的明显将this抛出）。

（2）在构造器中内部类使用外部类情况：内部类访问外部类是没有任何条件的，也不要任何代价，也就造成了当外部类还未初始化完成的时候，内部类就尝试获取为初始化完成的变量。

那么，如何避免this逃逸呢？

导致的this引用逸出需要满足两个条件：

1、在构造函数中创建内部类(EventListener)

2、是在构造函数中就把这个内部类给发布了出去(source.registerListener)。

因此，我们要防止这一类this引用逸出的方法就是避免让这两个条件同时出现。也就是说，如果要在构造函数中创建内部类，那么就不能在构造函数中把他发布了，应该在构造函数外发布，即等构造函数执行完初始化工作，再发布内部类。

根据不同的场景，解决如下：

1、单独编写一个启动线程的方法，不要在构造器中启动线程，尝试在外部启动。

2、使用一个私有的构造函数进行初始化和一个公共的工厂方法进行发布。

3、将事件监听放置于构造器外，比如new Object()的时候就启动事件监听，但是在构造器内不能使用事件监听，那可以在static{}中加事件监听，这样就跟构造器解耦了。

## **补充知识点**

```java
class Glyph {
    void draw() { //没有执行
        System.out.println("Glyph.draw()");
    }
    Glyph() {     //3，默认调用
        System.out.println("Glyph() before draw()");
        draw();   //父类构造器作为子类构造器执行前的默认执行，此时父构造器内执行的方法是子类的重写方法。
        System.out.println("Glyph() after draw()");
    }
}

class RoundGlyph extends Glyph {
    private int radius = 1;  //5，初始化变量

    RoundGlyph(int r) {//2，首先调用父类构造器（并且默认是无参构造器）
        radius = r;    //6，赋值执行
        System.out.println("RoundGlyph.RoundGlyph(). radius = " + radius);
    }

    void draw() {  //4，在父构造器被调用，此时该类（子类）还没被初始化，所以实例变量的值为默认值。
        System.out.println("RoundGlyph.draw(). radius = " + radius);
    }
}

public class PolyConstructors {
    public static void main(String[] args) {
        new RoundGlyph(5);//1，首先执行
    }
}
```

输出：

```
Glyph() before draw()
RoundGlyph.draw(). radius = 0  //未被初始化
Glyph() after draw()
RoundGlyph.RoundGlyph(). radius = 5
```

#### 原因——Java中构造函数的调用顺序：

- 在其他任何事物发生之前，将分配给对象的存储空间初始化成二进制0；
- 调用基类构造函数。从根开始递归下去，因为多态性此时调用子类覆盖后的draw()方法（要在调用RoundGlyph构造函数之前调用），由于步骤1的缘故，我们此时会发现radius的值为0；
- 按声明顺序调用成员的初始化方法；
- 最后调用子类的构造函数。

## **小结**

this引用逃逸问题实则是Java多线程编程中需要注意的问题，引起逃逸的原因无非就是在多线程的编程中  **滥用**  引用（往往涉及构造器中显式或隐式地滥用this引用），在使用到this引用的时候需要特别注意！

