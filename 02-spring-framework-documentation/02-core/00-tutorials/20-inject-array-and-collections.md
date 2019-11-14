# 注入数组与集合

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

## Array

```java
@Configuration
public class ArrayExample {
    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    @Bean
    public String[] strArray(){
        return new String[]{"two", "three", "four"};
    }
```

```java
 public class TestBean {
        private  String[] stringArray;

        @Autowired
        public void setStringArray (String[] stringArray) {
            this.stringArray = stringArray;
        }

           ....
    }
```

## 集合注入

```java
public class ListExample {
    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    @Bean
    public List<String> strList(){
        return Arrays.asList("two", "three", "four");
    }
```

```java
 public class TestBean {
        private  List<String> stringList;

        @Autowired
        public void setStringList (List<String> stringList) {
            this.stringList = stringList;
        }
            ...

    }
```

## **注入独立 beans as array/collections/map elements**

```java
@Configuration
public class SetInjection {

    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    @Bean
    public RefBean refBean1 () {
        return new RefBean("bean 1");
    }

    @Bean
    public RefBean refBean2 () {
        return new RefBean2("bean 2");
    }

    @Bean
    public RefBean refBean3 () {
        return new RefBean3("bean 3");
    }

    public static class TestBean {

        // All bean instances of type RefBean will be injecting here
        @Autowired
        private Set<RefBean> refBeans;
            ...
    }

    public static class RefBean{
        private String str;

            public RefBean(String str){
             this.str = str;
            }
         .....
    }
}
```



我们也可以使用下面方式注入所有指定类型的 bean

```java
@Autowired
private Map<String, RefBean> map; 
```

在运行时打印这个 map

```java
{refBean1=RefBean{str='bean 1'}, refBean2=RefBean{str='bean 2'}, refBean3=RefBean{str='bean 3'}}
```

## 使用@Qualifier 指定 **array/collection/map** 标签

```java
@Configuration
public class SetInjection {

    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    @Bean
    public RefBean refBean1 () {
        return new RefBean("bean 1");
    }

    @Bean
    @Qualifier("myRefBean")
    public RefBean refBean2 () {
        return new RefBean2("bean 2");
    }

    @Bean
    @Qualifier("myRefBean")
    public RefBean refBean3 () {
        return new RefBean3("bean 3");
    }

    public static class TestBean {

        @Autowired
        @Qualifier("myRefBean")//这里只会注入refBean2 和 myRefBean
        private Set<RefBean> refBeans;
            ...
    }

```

## 对 arrays /list进行排序

```java
@Configuration
public class ArrayExample {
    @Bean
    public TestBean testBean () {
        return new TestBean();
    }

    @Bean
    @Order(3)
    public String refString1 () {
        return "my string 1";
    }

    @Bean
    @Order(1)
    public String refString2 () {
        return "my string 2";
    }

    @Bean
    @Order(2)
    public String refString3 () {
        return "my string 3";
    }

    private static class TestBean {
        private String[] stringArray;

        @Autowired
        public void setStringArray (String[] stringArray) {
            this.stringArray = stringArray;
        }

        public String[] getStringArray () {
            return stringArray;
        }
    }
}
```

排序后的输出

```
[my string 2, my string 3, my string 1]
```

