# 里氏替换原则（Liskov Substitution Principle）

## 定义

>  **所有引用基类的地方必须能透明地使用其子类的对象。**

简单来说:**子类对象能够替换父类对象，而程序逻辑不变。**

## 解释

> JAVA中，多态是不是违背了里氏替换原则？？ - 知乎
> https://www.zhihu.com/question/27191817

里氏替换原则:

- 如果使用继承来做到代码重用,那么子类不应该覆盖父类中的方法,子类只能通过添加新的方法来拓展父类,**将子类替换掉父类时** ,由于没有更改父类的逻辑,所以安全替换
- 如果是多态,多态的前提就是子类覆盖并重新定义父类的方法,我们应该讲父类设置为抽象类,并定义抽象方法,让子类重新定义这些方法,由于抽象类无法被实例化,就不存在子类替换父类时的逻辑

## 作用

- 里氏替换原则是实现开闭原则的重要方式之一。

- 里氏替换原则克服了继承中重写父类造成的可复用性变差的缺点。

- 里氏替换原则是动作正确性的保证。即类的扩展不会给已有的系统引入新的错误，降低了代码出错的可能性。

## 使用

- **尽量不要从可实例化的父类中继承，而是要使用基于抽象类和接口的继承。**

- 里氏替换原则是实现开闭原则的基础，在程序中尽量使用基类类型对对象进行定义，而在运行时再确定其子类，用子类对象来代替父类对象。

- 子类可以扩展父类的功能，但不能改变父类原有的功能。也就是说：子类继承父类时，除添加新的方法完成新增功能外，尽量不要重写父类的方法。