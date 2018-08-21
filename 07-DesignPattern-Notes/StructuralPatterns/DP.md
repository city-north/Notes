[返回目录](/README.md)

[返回结构性模式列表](/StructuralPatterns/README.md)

# 装饰模式(Decorator Pattern)

装饰模式：动态地给一个对象增加一些额外的职责，就拓展而言，装饰模式提供了一种比使用子类更加灵活的替代方案。



![](assets/DP-01.jpg)

## 角色

**Component(抽象构件)**：它是具体构建和抽象装饰类的共同父类，声明了在具体构建中实现的业务方法，它的引入可以使客户端以一致的方式处理未被装饰的对象以及装饰之后的对象，实现客户端的透明操作。

**ConcreteComponent（具体构件）**：是抽象构件的子类，用于定义具体的构件对象，实现了在抽象构件中声明的方法，装饰类可以给它增加额外的职责（方法）。

**Decorator(抽象装饰类)**：抽象构件类的子类，用于给具体构件增加职责，但是具体职责通常在其子类中实现。维护一个指向抽象构件对象的引入。通过该引用可以调用装饰之前构件对象的方法，并通过其子类拓展该方法，达到装饰的作用。

**ConcreteDecorator(具体装饰类)**：它是抽象装饰类的子类，负责向构件添加新的职责。

## 代码示例

```java
public abstract class Component{
    public abstract void operation();
}
```



```java
public ConcreteComponent extends Component{
    @Override
    public void operation(){
        return System.out.println("ConcreteComponent.operation()");
    }
}
```

```java
public class Decorator implements Component {
    public Component component;

    public Decorator(Component component) {
        
        this.component = component;
    }
    
    @Override
    public void operation(){
        return System.out.println("ConcreteComponent.operation()");
    }
}
```

```java
//具体装饰类
public class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Component component) {
        super(component);
    }
    
    @Override
    public void operation(){
        addedBehavior();
        System.out.println("ConcreteComponent.operation()");
    }
    
    public void addedBehavior(){
        System.out.println("ConcreteDecorator.AddedBehavior()");
    }
}
```

```java
　　//使用装饰器
　　Component component = new ConcreteDecorator(new ConcretComponent());
　　component.operation();

　　//console：
    ConcreteDecorator.AddedBehavior()
    ConcreteComponent.operation()
```

