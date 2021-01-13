# 140-SpringTest中测试数据的属性源

[TOC]

## 简介

Spring4.1之后提供了测试配置属性源- @TestPropertySource

## @TestPropertySource优先级最高

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestPropertySourceTest.class) // Spring 注解驱动测试注解
@TestPropertySource(
        properties = "user.name = 小马哥", // PropertySource(name=Inlined Test Properties)
        locations = "classpath:/META-INF/test.properties"
)
public class TestPropertySourceTest {

    @Value("${user.name}")  // "mercyblitz" Java System Properties
    private String userName;

    @Autowired
    private ConfigurableEnvironment environment;

    @Test
    public void testUserName() {
        assertEquals("小马哥", userName);

        for (PropertySource ps : environment.getPropertySources()) {
            System.out.printf("PropertySource(name=%s) 'user.name' 属性：%s\n", ps.getName(), ps.getProperty("user.name"));
        }

    }

}

```

```java
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-test</artifactId>
  <scope>test</scope>
  <version>${spring.version}</version>
</dependency>
```

src/test/resources/META-INF/test.properties

```
user.name = 马昕曦
```

