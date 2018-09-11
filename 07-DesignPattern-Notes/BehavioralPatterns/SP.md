[返回根目录](/README.md)

[返回目录](../README.md)

# 状态模式（State Pattern）

允许一个对象在其内部状态改变时改变它的行为。对象看起来似乎修改了它的类。

## 简介

在软件系统中，有些对象也想水一样有多种状态，这些状态在某些情况下可以互相转换，而且对象在不同的状态下可将具有不同的行为。通常可以使用复杂的条件判断语句（if...else）来进行状态的判断和转换操作，这会导致代码的可维护性和灵活性下降，特别是出现一种新的状态时，代码的拓展性很差，客户端代码也需要进行相应的修改，违背了**开闭原则**。

![img](assets/1336719144_5496-1536632498551.jpg)

- 环境类（Context）:  定义客户感兴趣的接口。维护一个ConcreteState子类的实例，这个实例定义当前状态。
- 抽象状态类（State）:  定义一个接口以封装与Context的一个特定状态相关的行为。
- 具体状态类（ConcreteState）:  每一子类实现一个与Context的一个状态相关的行为。



## 例子

在本例中，我们使用一个常见的场景，比如我们用手机打开了CSDN，找到了一篇文章，觉得写的不错，当我们点击“收藏”时，会突然跳出一个登录界面，这时我们才发觉原来我们还没有登录，如果我们已经登录成功，在点击“收藏”，就会有相应的业务逻辑处理。

### **传统的实现方案**

对于上述案例，我们很容易想到在用户点击“收藏”时，通过if/else判断用户的登录状态，从而实现不同的逻辑，伪代码如下：

```

if(已经登录){
   //收藏逻辑
}else{
   //登录逻辑

```

上述解决方案缺点非常明显：这类代码难以应对变化，在添加一种状态时，我们需要手动添加if/else，在添加一种功能时，要对所有的状态进行判断。因此代码会变得越来越臃肿，并且一旦没有处理某个状态，便会发生极其严重的BUG，难以维护。

### **使用状态模式解决问题**

 **State接口类**

```

public interface State{
    public void collect (Context context);
}
```

**State接口实现类**

```
public class LogInState implements State {
    @override
    public void collect (Context context){
    Toast.makeText(context,"收藏成功",Toast.LENGTH_LONG).show();
    }
}
 
public class LogOutState implements State {
    @override
    public void collect (Context context){
      context.startActivity(new Intent(context,LogInActivity.class));
    }
}
```

**Context类**

```
public class LogInContext{
    State mState = new LogOutState();//初始化为未登录状态
    static LogInContext mLogInContext = new LogInContext();
    private LogInContext(){}
//使用单例模式提供管理类实例
public static LogInContext getLogInContextInstance(){
    return mLogInContext;
}
public void collect (Context context){
	mState.collect(context);
}
public void setState (State state){
    	this.mState = state;
    }
}
```

**使用时**

```

//用户在MainActivity点击收藏按钮
LogInContext.getLogInContextInstance.collect(MainActivity.this);
//注销账户
LogInContext.getLogInContextInstance.setState(new LogOutState());
 
//登录界面LogInActivity
if(登录验证成功){
    LogInContext.getLogInContextInstance.setState(new LogInState());
    finish();
}
```

## **状态模式的优缺点**

状态模式的优点：

在不同状态需要有不同响应的使用场景下，避免了使用if/else导致代码臃肿，使代码结构清晰的同时保证了拓展性和维护性。

 

状态模式的缺点：

状态模式的使用必然会增加类和对象的个数，如果使用不当将导致程序结构和代码的混乱。 

## **策略模式和状态模式比较**

策略模式和状态模式的结构几乎完全一致，但是它们的目的和本质完全不一样。

策略模式是围绕可以互换的[算法](http://lib.csdn.net/base/datastructure)来创建成功业务的，然而状态模式是通过改变对象内部的状态来帮助对象控制自己行为的。前者行为是彼此独立、可以相互替换的，后者行为是不可以相互替换的。





[返回根目录](/README.md)

[返回目录](../README.md)