# 序列化和反序列化

## 什么序列化,为什么我们要序列化

Java 对象在 JVM 运行的时候被创建, 更新, 或者销毁, 当 JVM 退出时,对象也会随之销毁

在现实应用中,我们常常需要将对象以及其状态在多个应用之间传递, 共享, 例如在一个应用中将对象以及其状态持久化, 在其他地方重新读取被保存的对象以及其状态继续进行处理

**序列化就是将对象以及其状态保存成一组字节数组中,其中**

- **序列化和反序列化必须序列化 ID 一致**,如果不一致会报错,没有写,jvm 会默认生成一个(安全校验)

```java
private static final long serialVersionUid = 1234567889L
```

- **静态变量不会被序列化**
- **transient 修饰的变量不会被序列化**
- 可以通过写 writeObject 方法和 readObject 方法定制序列化实现



## Transient

Transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变 量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null。

#### 绕开 transient 机制的办法

在标有 transient 在序列化时会被忽略,我们可以通过在实体类中写`writeObject`方法和`readObject`方法在自定义序列化机制

#### writeObject 和 readObject 原理

writeObject 和 readObject 是两个私有的方法，他们是`ObjectInputStream` 和 `ObjectOutPutStream`中触发的



![image-20200329201332803](assets/image-20200329201332803.png)

readObject 方法会在反序列化时通过反射调用

![image-20200329201426798](assets/image-20200329201426798.png)

#### Java 序列化的一些简单总结

1. Java 序列化只是针对对象的状态进行保存，至于对象中的方法，序列化不关心
2. 当一个父类实现了序列化，那么子类会自动实现序列化，不需要显示实现序列化接口
3. 当一个对象的实例变量引用了其他对象，序列化这个对象的时候会自动把引用的对象也进
 行序列化(实现深度克隆)
4. 当某个字段被申明为 transient 后，默认的序列化机制会忽略这个字段
5. 被申明为 transient 的字段，如果需要序列化，可以添加两个私有方法:writeObject 和
readObject



