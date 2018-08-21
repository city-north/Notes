[返回目录](/README)

[返回创建型模式简介](/CreationalPatterns/README.md)

# 原型模式（Prototype Pattern）

原型模式时用于创建重复的对象，同时又能保证性能。

![](assets/723909-20150913172532590-618965482.png)

- Prototype(抽象原型类)：声明克隆方法的接口，是所有具体原型类的公共父类，它可是抽象类也可以是接口，甚至可以是具体实现类。
- ConcretePrototype(具体原型类)：它实现抽象原型类中声明的克隆方法，在克隆方法中返回自己的一个克隆对象。
- Client(客户端)：在客户类中，让一个原型对象克隆自身从而创建一个新的对象。

```java
class ConcretePrototype implements Cloneable
{
    public Prototype  clone()
    {
      Object object = null;
      try {
         object = super.clone();
      } catch (CloneNotSupportedException exception) {
         System.err.println("Not support cloneable");
      }
      return (Prototype )object;
    }
}
```