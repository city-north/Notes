# Spring Boot default logging Configuration

springBoot 默认使用

- 转发所有
  - Apache Commons Logging (JCL), 
  - Log4J 
  - Java Util Logging (JUL) APIs 

三个日志门面的数据到

- SLF4J

使用 Logback 作为 SLF4J 的实现