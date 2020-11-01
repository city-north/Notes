# counterparts

- n. （契约）副本（counterpart的复数）；相对物；相对应的人



This class provides thread-local variables. These variables differ from their normal **counterparts** in that each thread that accesses one (via its get or set method) has its own, independently initialized copy of the variable.

 ThreadLocal instances are typically private static fields in classes that wish to associate state with a thread (e.g., a user ID or Transaction ID).

For example, the class below generates unique identifiers local to each thread. A thread's id is assigned the first time it invokes ThreadId.get() and remains unchanged on **subsequent** calls.



```java
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadId {
// Atomic integer containing the next thread ID to be assigned
private static final AtomicInteger nextId = new AtomicInteger(0);
 // Thread local variable containing each thread's ID
   private static final ThreadLocal<Integer> threadId =
       new ThreadLocal<Integer>() {
           @Override protected Integer initialValue() {
               return nextId.getAndIncrement();
       }
   };
  
   // Returns the current thread's unique ID, assigning it if necessary
   public static int get() {
       return threadId.get();
   }
}
```


Each thread holds an reference to its copy of a thread-local variable as long as the thread is alive and the ThreadLocal instance is accessible; 

after a thread goes away, all of its copies of thread-local instances are subject to garbage collection (unless other references to these copies exist).