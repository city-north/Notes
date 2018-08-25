[返回目录](/README.md)

[返回结构性模式列表](/StructuralPatterns/README.md)

# 外观模式（FacadePatterns）

外观模式：为子系统中的一组接口提供一个统一的入口，外观模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。

![](assets/FP-01.jpg)



## 角色

Facade（外观角色）：在客户端可以调用它的方法。

SubSystem（子系统角色）：子系统并不知道外观的存在，对于子系统而言，外观角色仅仅是另外一个客户端而已

## 实例

![](assets/FP-02.jpg)



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