# 外观模式（FacadePatterns）

外观模式（Facade Patterns）也叫门面模式, 通过一个门面（Facade） 向 客户端提供一个访问系统的统一接口，客户端无需关心和知晓系统内部的各个子模块（系统）之间的复杂关系

其主要目的是降低访问拥有多个子系统的复杂系统的难度，简化客户端与其之间的接口，客户端通过一个接口访问系统，使得系统使用起来更加容易

> 外观模式：为子系统中的一组接口提供一个统一的入口，外观模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。

## UML
![](assets/5bb98211046b2.jpg)


## 角色

Facade（外观角色）：在客户端可以调用它的方法。

SubSystem（子系统角色）：子系统并不知道外观的存在，对于子系统而言，外观角色仅仅是另外一个客户端而已

以汽车的启动为例，用户只需要按下启动按钮，后台就会自动完成启动，仪表盘启动，车辆自检等过程

我们通过外观模式将汽车启动这一系列流程封装到启动按钮上

对于用户来说我们只要按下启动该按钮即可



## 实例

```
public interface Shape {
   void draw();
}
```

```
public class Rectangle implements Shape {
 
   @Override
   public void draw() {
      System.out.println("Rectangle::draw()");
   }
}
```

```
public class Circle implements Shape {
 
   @Override
   public void draw() {
      System.out.println("Circle::draw()");
   }
}
```

```
public class Square implements Shape {
 
   @Override
   public void draw() {
      System.out.println("Square::draw()");
   }
}
```

```
public class ShapeMaker {
   private Shape circle;
   private Shape rectangle;
   private Shape square;
 
   public ShapeMaker() {
      circle = new Circle();
      rectangle = new Rectangle();
      square = new Square();
   }
 
   public void drawCircle(){
      circle.draw();
   }
   public void drawRectangle(){
      rectangle.draw();
   }
   public void drawSquare(){
      square.draw();
   }
}
```

```
public class FacadePatternDemo {
   public static void main(String[] args) {
      ShapeMaker shapeMaker = new ShapeMaker();
 
      shapeMaker.drawCircle();
      shapeMaker.drawRectangle();
      shapeMaker.drawSquare();      
   }
}
```

## 优点

1. 对客户端屏蔽了子系统组件
2. 实现了子系统和客户端之间的松耦合
3. 内部变化不会影响到外观变化

## 缺点

1. 如果设计不当可能需要修改外观类的源代码，违背了开闭原则
2. 外观模式不能很好地限制客户端直接使用子系统类，如果读客户端访问子系统类做太多限制，则减少了可变性和灵活性。