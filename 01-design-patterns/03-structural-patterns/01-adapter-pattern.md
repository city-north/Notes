# Adapter Pattern（适配器模式）

> [参考文章](https://www.jianshu.com/p/9d0575311214)

> 把一个类的接口变换成客户端所期待的另一个接口，从而使因原本接口不匹配而无法在一起工作的两个类能够在一起工作。

## 适用场景

- 已经存在的类,它的方法和需求不匹配(方法结果相同或者相似) 的情况
- 适配器模式不是软件设计阶段的设计模式,是随着软件维护,由于不同产品,不同厂家造成功能类似而接口不相同的情况下解决方案

分类：

1. 类适配器
2. 对象适配器

## 1.类适配器

![](assets/5bb98035b7d0d.png)

上图中：

* 冲突：Target期待调用Request方法，而Adaptee并没有（这就是所谓的不兼容了）。

* 解决方案：为使Target能够使用Adaptee类里的SpecificRequest方法，故提供一个中间环节Adapter类**（继承Adaptee & 实现Target接口）**，把Adaptee的API与Target的API衔接起来（适配）。

* Adapter与Adaptee是继承关系，这决定了这个适配器模式是类适配器

### 代码

**步骤1**：创建**Target接口**

```
public interface Target {

    //这是源类Adapteee没有的方法
    public void Request(); 
}
```

**步骤2**：创建**源类（Adaptee）**

```
public class Adaptee {

    public void SpecificRequest(){
    }
}
```

**步骤3**：创建**适配器类（Adapter）**

```java
//适配器Adapter继承自Adaptee，同时又实现了目标(Target)接口。
public class Adapter extends Adaptee implements Target {

    //目标接口要求调用Request()这个方法名，但源类Adaptee没有方法Request()
    //因此适配器补充上这个方法名
    //但实际上Request()只是调用源类Adaptee的SpecificRequest()方法的内容
    //所以适配器只是将SpecificRequest()方法作了一层封装，封装成Target可以调用的Request()而已
    @Override
    public void Request() {
        this.SpecificRequest();
    }

}
```

**步骤4**：定义具体使用目标类，并通过Adapter类调用所需要的方法从而实现目标

```java
public class AdapterPattern {

    public static void main(String[] args){

        Target mAdapter = new Adapter()；
        mAdapter.Request（）;

    }
}
```

## 2.对象适配器模式

与类的适配器模式相同，对象的适配器模式也是把适配的类的API转换成为目标类的API。

与类的适配器模式不同的是，对象的适配器模式不是使用继承关系连接到Adaptee类，而是使用委派关系连接到Adaptee类。

![](/assets/import02.png)



在上图中可以看出：

* 冲突：Target期待调用Request方法，而Adaptee并没有（这就是所谓的不兼容了）。

解决方案：为了使Target能使用Adaptee类里面的SpecificRequest方法，需要在提供一个中间环节Adapter类**（包装了一个Adaptee的实例）,**把Adaptee的API,与Target的API衔接起来（适配）。

Adapter与Adaptee是委派关系，这决定了适配器模式是对象适配器。

### 实战

**步骤1**：创建T**arget接口**；

```
public interface Target {

    //这是源类Adapteee没有的方法
    public void Request(); 
}
```

**步骤2**：创建**源类（Adaptee）**；

```
public class Adaptee {

    public void SpecificRequest(){
    }
}
```

**步骤3**：创建**适配器类（Adapter）**（不适用继承而是委派）

```
class Adapter implements Target{  
    // 直接关联被适配类  
    private Adaptee adaptee;  

    // 可以通过构造函数传入具体需要适配的被适配类对象  
    public Adapter (Adaptee adaptee) {  
        this.adaptee = adaptee;  
    }  

    @Override
    public void Request() {  
        // 这里是使用委托的方式完成特殊功能  
        this.adaptee.SpecificRequest();  
    }  
}
```

**步骤4**：定义具体使用目标类，并通过Adapter类调用所需要的方法从而实现目标

```
public class AdapterPattern {
    public static void main(String[] args){
        //需要先创建一个被适配类的对象作为参数  
        Target mAdapter = new Adapter(new Adaptee())；
        mAdapter.Request();

    }
}
```

# 3. 优缺点

#### 3.1 适配器模式

**优点**

* 更好的复用性

  系统需要使用现有的类，而此类的接口不符合系统的需要。那么通过适配器模式就可以让这些功能得到更好的复用。

* 透明、简单

  客户端可以调用同一接口，因而对客户端来说是透明的。这样做更简单 & 更直接

* 更好的扩展性

  在实现适配器功能的时候，可以调用自己开发的功能，从而自然地扩展系统的功能。

* 解耦性

  将目标类和适配者类解耦，通过引入一个适配器类重用现有的适配者类，而无需修改原有代码

* 符合开放-关闭原则

  同一个适配器可以把适配者类和它的子类都适配到目标接口；可以为不同的目标接口实现不同的适配器，而不需要修改待适配类

**缺点**

过多的使用适配器，会让系统非常零乱，不易整体进行把握

#### 3.2 类的适配器模式

优点

* 使用方便，代码简化，仅仅引入一个对象，并不需要额外的字段来引用Adaptee实例

缺点

* 高耦合，灵活性低，使用的对象继承的方式，是静态的定义方式。

#### 3.3 对象的适配器模式

**优点**

* 灵活性高，低耦合，采用“对象组合”的方式，是动态组合方式

缺点

* 使用复杂，需要引入对象实例

特别是需要重新定义Adaptee行为时，需要重新定义Adaptee的子类，并将适配器组合适配

# 4. 应用场景

#### 4.1 适配器的使用场景

* 系统需要复用现有类，而该类的接口不符合系统的需求，可以使用适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作
* 多个组件功能类似，但接口不统一且可能会经常切换时，可使用适配器模式，使得客户端可以以统一的接口使用它们

#### 4.2 类和对象适配器模式的使用场景

1. 灵活使用时：选择对象的适配器模式  
    类适配器使用对象继承的方式，是静态的定义方式；而对象适配器使用对象组合的方式，是动态组合的方式。

2. 需要同时配源类和其子类：选择对象的适配器

* 对于类适配器，由于适配器直接继承了Adaptee，使得适配器不能和Adaptee的子类一起工作，因为继承是静态的关系，当适配器继承了Adaptee后，就不可能再去处理 Adaptee的子类了；
* 对于对象适配器，一个适配器可以把多种不同的源适配到同一个目标。换言之，同一个适配器可以把源类和它的子类都适配到目标接口。因为对象适配器采用的是对象组合的关系，只要对象类型正确，是不是子类都无所谓。

1. 需要重新定义Adaptee的部分行为：选择类适配器

* 对于类适配器，适配器可以重定义Adaptee的部分行为，相当于子类覆盖父类的部分实现方法。
* 对于对象适配器，要重定义Adaptee的行为比较困难，这种情况下，需要定义Adaptee的子类来实现重定义，然后让适配器组合子类。虽然重定义Adaptee的行为比较困难，但是想要增加一些新的行为则方便的很，而且新增加的行为可同时适用于所有的源。

1. 仅仅希望使用方便时：选择类适配器

* 对于类适配器，仅仅引入了一个对象，并不需要额外的引用来间接得到Adaptee。
* 对于对象适配器，需要额外的引用来间接得到Adaptee。

### 总结

建议尽量使用对象的适配器模式，多用合成/聚合、少用继承。

## 装饰者模式和适配器模式的区别

|      | 装饰者模式                                                   | 适配器模式                                                   |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 形式 | 是一种特殊的适配器模式,有层级关系                            | 没有层级关系,                                                |
| 定义 | 装饰者和被装饰者都实现同一个接口,主要目的是拓展之后依然保留 OPP关系 | 适配器和被适配者没有必然的联系,通常是采用集成或者代理的形式进行包装 |
| 关系 | 满足 is-a 关系                                               | 满足 has-a 关系                                              |
| 功能 | 注重覆盖、拓展                                               | 注重兼容、转换                                               |
| 设计 | 前置考虑,在设计的时候,就梳理清楚层级关系                     | 后置考虑,初夏需要拓展的需求的时候使用                        |

