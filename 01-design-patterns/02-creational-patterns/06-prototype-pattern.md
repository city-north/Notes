# 原型模式（Prototype Pattern）

## 定义

> 使用原型实例指定待创建对象的类型并且通过复制这个原型来创建新的对象

## 理解

- 原型模式用于于创建重复的对象，同时又能保证性能。

- 当一个对象需要在一个高代价的数据库操作后被创建,我们可以缓存该对象,下一个请求时返回它的克隆,在需要的时候更新数据库,以此来减少数据库调用

## 注意事项

原型模式通过拷贝一个现有对象生成新的对象,浅拷贝实现 `Cloneable`接口,重写,深拷贝是通过实现`Serializable`读取二进制流

## UML

![img](assets/723909-20150913172532590-618965482.png?lastModify=1571634609)

- Prototype(抽象原型类)：声明克隆方法的接口，是所有具体原型类的公共父类，它可是抽象类也可以是接口，甚至可以是具体实现类。
- ConcretePrototype(具体原型类)：它实现抽象原型类中声明的克隆方法，在克隆方法中返回自己的一个克隆对象。
- Client(客户端)：在客户类中，让一个原型对象克隆自身从而创建一个新的对象。

```
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

## 使用场景

- 资源优化场景
- 类初始化需要消耗非常多的资源,这个资源包括数据、硬件资源等
- 性能和安全要求高的场景
- 通过 new 产生一个对象需要非常繁琐的数据准备或者访问权限
- 一个对象多个修改者的场景
- 一个对象需要提供给其他对象访问,而且各个调用者可能都需要修改其值时.可以考虑使用原型模式拷贝多个对象提供给调用者使用
- 一般与工厂方法模式出现,通过 clone 方法创建一个对象

## 实例

形状抽象类,实现 Cloneable 接口

```java
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
```

#### Shape 实现类 Circle

```java
public class Circle extends Shape {

    public Circle(){
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
```

#### Shape 实现类 Rectangle

```java
public class Rectangle extends Shape {

    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}

```

#### Shape 实现类 Square

```java
public class Square extends Shape {

    public Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}

```

#### Shape 缓存

```java
public class ShapeCache {

    private static Hashtable<String, Shape> shapeMap
            = new Hashtable<String, Shape>();

    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    public static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}
```

#### 测试类

```java
public class Test {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape clonedShape = ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());

        Shape clonedShape2 = ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape clonedShape3 = ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());
    }
}
```

#### 运行结果

```
Shape : Circle
Shape : Square
Shape : Rectangle
```