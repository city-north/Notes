## 工厂方法模式（Factory Method Pattern）

[参考](https://blog.csdn.net/carson_ho/article/details/52343584)

定义一个用于创建对象的接口，但是让子类决定将哪一个类实例化。工厂方法模式让一个类的实例化延迟都其子类。

工厂方法模式，简称为**工厂模式（Factory Pattern）**，又可成为**虚拟构造器模式(Virtual Constructor Pattern)**或**多态工厂模式（Polymorphic Factory Pattern）**。

## UML

![](assets/20160828082911344.png)

## 核心

具体的产品由具体的**工厂**来进行创建。

## 角色

| 角色             | 介绍                                   |
| ---------------- | -------------------------------------- |
| AbstractFactory  | 抽象工厂类，具体描述产品的公共接口     |
| ConcreateFactory | 某种产品的具体工厂，实现抽象工厂类接口 |
| Product          | 抽象产品                               |
| ConcreteProduct  | 具体产品                               |

**步骤1：** 创建**抽象工厂类**，定义具体工厂的公共接口 

```java
abstract class Factory{
    public abstract Product Manufacture();
}
```

**步骤2：** 创建**抽象产品类** ，定义具体产品的公共接口； 

```java
abstract class Product{
    public abstract void Show();
}
```

**步骤3：** 创建**具体产品类**（继承抽象产品类）， 定义生产的具体产品； 

```java
//具体产品A类
class  ProductA extends  Product{
    @Override
    public void Show() {
        System.out.println("生产出了产品A");
    }
}

//具体产品B类
class  ProductB extends  Product{

    @Override
    public void Show() {
        System.out.println("生产出了产品B");
    }
}
```

**步骤4：**创建**具体工厂类**（继承抽象工厂类），定义创建对应具体产品实例的方法； 

```java
//工厂A类 - 生产A类产品
class  FactoryA extends Factory{
    @Override
    public Product Manufacture() {
        return new ProductA();
    }
}

//工厂B类 - 生产B类产品
class  FactoryB extends Factory{
    @Override
    public Product Manufacture() {
        return new ProductB();
    }
}
```

**步骤5：**外界通过调用具体工厂类的方法，从而创建不同**具体产品类的实例** 

```java
//生产工作流程
public class FactoryPattern {
    public static void main(String[] args){
        //客户要产品A
        FactoryA mFactoryA = new FactoryA();
        mFactoryA.Manufacture().Show();

        //客户要产品B
        FactoryB mFactoryB = new FactoryB();
        mFactoryB.Manufacture().Show();
    }
}
```

结果：

```
生产出了产品A
生产出了产品C
```

## 一个容易理解的例子

工厂模式也就是鼠标工厂是个父类，有生产鼠标这个接口。

戴尔鼠标工厂，惠普鼠标工厂继承它，可以分别生产戴尔鼠标，惠普鼠标。

生产哪种鼠标不再由参数决定，而是创建鼠标工厂时，由戴尔鼠标工厂创建。

后续直接调用鼠标工厂.生产鼠标()即可

### UML

![](assets/20160828082911344.png)

## 优点

- 更符合开-闭原则 
  新增一种产品时，只需要增加相应的具体产品类和相应的工厂子类即可

  > 简单工厂模式需要修改工厂类的判断逻辑

- 符合单一职责原则 
  每个具体工厂类只负责创建对应的产品

  > 简单工厂中的工厂类存在复杂的switch逻辑判断

- 不使用静态工厂方法，可以形成基于继承的等级结构。

  > 简单工厂模式的工厂类使用静态工厂方法

总结：工厂模式可以说是简单工厂模式的进一步抽象和拓展，在保留了简单工厂的封装优点的同时，让扩展变得简单，让继承变得可行，增加了多态性的体现。

## 缺点

- 添加新产品时，除了增加新产品类外，还要提供与之对应的具体工厂类，系统类的个数将成对增加，在一定程度上增加了系统的复杂度；同时，有更多的类需要编译和运行，会给系统带来一些额外的开销；
- 由于考虑到系统的可扩展性，需要引入抽象层，在客户端代码中均使用抽象层进行定义，增加了系统的抽象性和理解难度，且在实现时可能需要用到DOM、反射等技术，增加了系统的实现难度。
- 虽然保证了工厂方法内的对修改关闭，但对于使用工厂方法的类，如果要更换另外一种产品，仍然需要修改实例化的具体工厂类；
- 一个具体工厂只能创建一种具体产品

## 应用场景

在了解了优缺点后，我总结了工厂方法模式的应用场景：

- 当一个类不知道它所需要的对象的类时在工厂方法模式中，客户端不需要知道具体产品类的类名，只需要知道所对应的工厂即可；
- 当一个类希望通过其子类来指定创建对象时在工厂方法模式中，对于抽象工厂类只需要提供一个创建产品的接口，而由其子类来确定具体要创建的对象，利用面向对象的多态性和里氏代换原则，在程序运行时，子类对象将覆盖父类对象，从而使得系统更容易扩展。
- 将创建对象的任务委托给多个工厂子类中的某一个，客户端在使用时可以无须关心是哪一个工厂子类创建产品子类，需要时再动态指定，可将具体工厂类的类名存储在配置文件或数据库中。

## 代码实例

```java
interface IProductFactory {

    Product create();
}


/**
 * factory method factory
 *
 * @author EricChen 2019/12/31 21:59
 */
class IphoneFactory implements IProductFactory {

    public Product create() {
        return new Iphone();
    }
}

```

```java
/**
 * 工厂方法实例
 *
 * @author EricChen 2019/12/31 21:09
 */
public class FactoryMethodExample {


    public static void main(String[] args) {
        SimpleFactoryExample.doWithoutPatterns();//不使用
        doWithFactoryMethod();
    }

    private static void doWithFactoryMethod() {
        IphoneFactory iphoneFactory = new IphoneFactory();
        Product product = iphoneFactory.create();
        System.out.println("使用工厂方法创建一个对象" + product);
    }

}
```

