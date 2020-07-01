# Unsafe

Unsafe 类是在 sun.misc 包下，不属于 Java 标准。但是很多 Java 的基础类库，包 括一些被广泛使用的高性能开发库都是基于 Unsafe 类开发的，比如 Netty、 Hadoop、Kafka 等;

Unsafe 可认为是 Java 中留下的后门，提供了一些低层次操作，如直接内存访问、 线程的挂起和恢复、CAS、线程同步、内存屏障

而 CAS 就是 Unsafe 类中提供的一个原子操作，

- 第一个参数为需要改变的对象， 

- 第二个为偏移量(即之前求出来的 headOffset 的值)，

- 第三个参数为期待的值，

- 第四个为更新后的值整个方法的作用是如果当前时刻的值等于预期值 var4 相等，则更新为新的期望值 var5，如果更新成功，则返回 true，否则返回 false;

## 

```java
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);
```

## stateOffset

一个 Java 对象可以看成是一段内存，每个字段都得按照一定的顺序放在这段内存 里，通过这个方法可以准确地告诉你某个字段相对于对象的起始内存地址的字节 偏移。用于在后面的 compareAndSwapInt 中，去根据偏移量找到对象在内存中的具体位置

所以 stateOffset 表示 state 这个字段在 AQS 类的内存中相对于该类首地址的偏移 量

## compareAndSwapInt

在 unsafe.cpp 文件中，可以找到 compareAndSwarpInt 的实现

```
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject
unsafe, jobject obj, jlong offset, jint e, jint x)) UnsafeWrapper("Unsafe_CompareAndSwapInt");
oop p = JNIHandles::resolve(obj); //将 Java 对象解析成 JVM 的 oop(普通
对象指针),
jint* addr = (jint *) index_oop_from_field_offset_long(p, offset); //根据对象 p
和地址偏移量找到地址
return (jint)(Atomic::cmpxchg(x, addr, e)) == e; //基于 cas 比较并替换， x 表
示需要更新的值，addr 表示 state 在内存中的地址，e 表示预期值
UNSAFE_END
```

3. 