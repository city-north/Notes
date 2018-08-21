[返回目录](/README.md)

# Lambda表达式

Lambda表达式（闭包）,Java8发布最重要的新特性。

Lambda允许把**函数**作为一个**方法的参数**（函数作为参数传递进方法中）。使用Lambda表达式可以使代码变得更加简洁，紧凑。

# 语法

```
(parameters) -> expression
或者
(parameters) -> {statements;}
```

* **可选类型声明：**不需要声明参数类型，编译器可以统一识别参数值。
* **可选的参数圆括号：**一个参数无需定义圆括号，但多个参数需要定义圆括号。
* **可选的大括号：**如果主题包含了一个语句，就不需要使用大括号。
* **可选的返回关键字：**如果主体只有一个表达式返回值，则编译器会自动返回值，大括号需要指明表达式返回一个数值。

## 例子

写一个比较的方法

Java8之前：

```
Comparator<Apple> byWeight = new Comparator<Apple>() {
    public int compare(Apple a1, Apple a2){
        return a1.getWeight().compareTo(a2.getWeight());
    }
};
```

Java8之后

![](/assets/import01.png)

```
Comparator<Apple> byWeight =(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```

分为三个部分：

* 参数列表—使用了Comparator中的compare方法中的参数，两个Apple.
* 箭头—把参数列表与Lambda主体分隔开
* Lambda主体—比较两个Apple的重量，表达式就是Lambda的返回值

![](/assets/import02.png)

```
布尔表达式  
(List<String> list) -> list.isEmpty()
创建对象 
 () -> new Apple(10)
消费一个对象
(Apple a) -> {
System.out.println(a.getWeight());
}
从一个对象中选择/抽取
(String s) -> s.length()
组合两个值  
(int a, int b) -> a * b
比较两个对象
(Apple a1, Apple a2) ->
a1.getWeight().compareTo(a2.getWeight())
```

## 实例

```
public class Lambda01 {

    interface MathOperation{
        int operation (int a,int b);
    }

    interface GreetingService{
        void sayMessage(String message);
    }

    //方法可以作为参数传入
    private int operation(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }

    public static void main(String[] args) {
        Lambda01 lambda01 = new Lambda01();
        //类型声明
        MathOperation addition = (int a, int b) -> a + b;

        //不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        //大括号中的返回语句
        MathOperation multiplication = (int a ,int b) -> {
            a = a+b;
            b = a-b;
            return a * b;
        };

        //没有大括号以及返回语句
        MathOperation division = (int a, int b) -> a/b;

        System.out.println("10 + 5 = " +lambda01.operation(10,5,addition));
        System.out.println("10 - 5 = " +lambda01.operation(10,5,subtraction));
        System.out.println("10 * 5 = "+lambda01.operation(10,5,multiplication));
        System.out.println("10 / 5 = " +lambda01.operation(10,5,division));

        //不用括号
        GreetingService greetingService1 = message -> System.out.println("hello," + message);
        //使用括号
        GreetingService greetingService2 = (message) -> System.out.println("hello2,"+message);

        GreetingService greetingService3 = message -> {
            String msg = "Kara";
            System.out.println("hello "+msg+","+message);
        };

        greetingService1.sayMessage("Jack");
        greetingService2.sayMessage("Eric");
        greetingService3.sayMessage("Tom");

    }
}
```

注意：

* Lambda表达式主要用来定义行内执行的方法类型接口。
* Lambda表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数话编程能力

## 什么是函数式接口

* 函数式接口仅仅定义一个抽象方法的接口。
* 接口还可以拥有默认方法（在类没有对方法进行实现时，其主体为方法提供默认实现的方法），该接口只能定义了一个**抽象方法，**哪怕有很多默认方法，依然是一个函数式接口。

## 变量作用域

Lambda表达式只能引用标记了final的外层局部变量，这就是说不能再lambda内部修改定义在域外的局部变量，否则会编译错误。

```
public class Java8Tester {

   final static String salutation = "Hello! ";

   public static void main(String args[]){
      GreetingService greetService1 = message -> 
      System.out.println(salutation + message);
      greetService1.sayMessage("Runoob");
   }

   interface GreetingService {
      void sayMessage(String message);
   }
}
```

```
public class Java8Tester {
    public static void main(String args[]) {
        final int num = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3
    }

    public interface Converter<T1, T2> {
        void convert(int i);
    }
}
```

lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）

```
int num = 1;  
Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
s.convert(2);
num = 5;  
//报错信息：Local variable num defined in an enclosing scope must be final or effectively 
 final
```

在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。

```
String first = "";  
Comparator<String> comparator = (first, second) -> Integer.compare(first.length(), second.length());  //编译会出错
```

[返回目录](#)

