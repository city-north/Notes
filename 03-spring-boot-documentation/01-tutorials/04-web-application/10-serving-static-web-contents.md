# 使用静态内容

By default Spring boot serves static content from one of the following locations in the classpath:

- /static

- /public

- /resources

- /META-INF/resources

## 四个默认的页面

#### static

```java
http://localhost:8080/page1.html
```

![image-20191204113341002](assets/image-20191204113341002.png)

访问

![image-20191204113314265](assets/image-20191204113314265.png)

#### public

![image-20191204113643552](assets/image-20191204113643552.png)

访问

![image-20191204113631130](assets/image-20191204113631130.png)

#### resources

![image-20191204113807703](assets/image-20191204113807703.png)

访问

![image-20191204113755427](assets/image-20191204113755427.png)

#### /META-INF/resources

![image-20191204113953651](assets/image-20191204113953651.png)

访问

![image-20191204113941199](assets/image-20191204113941199.png)

## 自定义静态资源路径

## 