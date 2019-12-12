# 自定义输出格式

配置属性logging.pattern.console

```
spring.main.banner-mode=off 
spring.output.ansi.enabled=ALWAYS
logging.pattern.console=%clr(%d{yy-MM-dd E HH:mm:ss.SSS}){blue} %clr(%-5p) %clr(${PID}){faint} %clr(---){faint} %clr([%8.15t]){cyan} %clr(%-40.40logger{0}){blue} %clr(:){red} %clr(%m){faint}%n
```