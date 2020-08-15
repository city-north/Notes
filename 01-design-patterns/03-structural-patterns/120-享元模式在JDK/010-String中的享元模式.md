# String中的享元模式

```
public class StringTest {
    public static void main(String[] args) {
        String s1 = "hello";//字符串常量池
        String s2 = "hello"; //字符串常量池
        String s3 = "he" + "llo";//字符串常量池
        //lo 在字符串常量池
        //new String("lo")在堆中
        //两者相加,结构在堆里 
        String s4 = "hel" + new String("lo");  
        String s5 = new String("hello");//堆中
        String s6 = s5.intern(); //字符串常量池
        String s7 = "h";
        String s8 = "ello";
        String s9 = s7 + s8;
//        String s10 = "h" + "ello";

//        System.out.println(s1 == s2); //true
//        System.out.println(s1 == s3);   //true
//        System.out.println(s1 == s4); //false
//        System.out.println(s1 == s5); //false
//        System.out.println(s4 == s5); //false
//        System.out.println(s1 == s6); //true
//        System.out.println(s1 == s9); //false

    }

}
```

Java中的 String 定义的由 final 修饰的 (不可变的)

JVM 将它存在字符串常量池中 [运行时常量池](../../../07-jvm/02-Java 内存区域与内存溢出异常/04-方法区.md#运行时常量池) 

- Jdk6 使用永久代实现常量池
- jdk7 以后移动到了堆
- jdk8 放到了元空间

