# 组合模式（Composite Pattern）

**组合模式**：组合多个对象形成属性结构以表示具有部分-整体的层次结构。组合模式让客户端可以统一对待单个对象和组合对象。


## UML
![](assets/5bb981b0d1a77.jpg)

## 角色：

- **Component**（抽象构建）：它可以是接口或者抽象类，为叶子构件和容器构建对象声明接口，在该角色中可以包含所有子类共有的声明和实现。在抽象构件中定义了访问以及管理它的子构件的方法。如增删改改查方法等。
- **Leaf**（叶子构件）：它在组合结构中表示叶子节点对象，叶子节点没有子节点，它实现了在抽象构件中地兵役的行为，对那些访问以及管理子构件的方法，可以通过抛出异常提示错误等方式进行处理。
- **Composite**（容器构建）：它在组合结构中表示容器结点对象，容器结点包含子节点，其子节点可以是叶子节点，也可以是容器结点，它提供一个集合用于存储子节点，实现在抽象构件中定义的行为，包括哪些访问以及管理子构件的方法，在其业务方法中可以通过递归调用其子节点的业务方法。

**Component**：

```java
public abstract class Component{
    public abstract void add(Component c);
    public abstract void remove(Component c);
    public abstract Component getChild(int i);
    public abstract void operation();//业务方法
}
```

**Leaf**：

```java
public Leaf extends Component{
   @Override
    public void add(Component c){
        //异常处理或错误信息
    }
   @Override
    public void remove(Component c){
        //异常处理或错误信息
    } 
   @Override
    public Component getChild(int i){
        //异常处理或错误信息
        return null;
    }
   @Override
    public void operation(Component c){
        //叶子构件具体的业务方法的实现
    }
}
```

**Composite**:

```java
public Composite extends Component{
    private List<Component> list = new List<Component>();
   @Override
    public void add(Component c){
        list.add(c);
    }
    
   @Override
    public void remove(Component c){
        list.remove(c);
    }
   @Override
    public Component getChild(int i){
        return (Component)list.get(i);
    } 
   @Override
    public void operation(Component component){
        for(Component c: list){
            c.operation();
        }
    } 
}
```

## 优点：

1. 组合模式可以清楚地定义分层次的复杂对象，表示对象的全部或部分层次，让客户端忽略了层次的差异，方便对整个层次结构进行控制。
2. 客户端可以一致地使用一个组合结构或其中单个对象，不必关心处理的是单个对象修改，符合开闭原则。
3. 在组合模式中增加新的容器构建和叶子构件都很方便，无需对现有类库进行任何修改，符合开闭原则。
4. 为树形结构的面向对象实现提供了一种灵活的解决方案，通过叶子对象容器对象的递归组合，可以形成复杂的树形结构，但对树形结构的控制缺非常简单。

## 缺点：

在增加新构建时很难对容器的构建类型进行限制。

## 应用场景：

- 在具有整体和部分的层次结构中，希望通过一种方式忽略整体与部分的差异，客户端可以一致地对待它们。
- 在一个使用面向对象语言开发的系统中需要处理一个树形结构。
- 在一个系统中能够分离出叶子对象和容器对象，而且他们的类型不固定，需要增加一些新的类型。