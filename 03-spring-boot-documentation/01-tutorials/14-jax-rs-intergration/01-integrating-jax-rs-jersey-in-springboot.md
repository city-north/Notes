# Integrating JAX-RS + Jersey in Spring Boot Application

Instead of Spring MVC, Boot application can be integrated with one of the available JAX-RS implementations (e.g. Jersey, Apache CXF etc). In this example, we will see how to use Jersey. Following are the steps to achieve that:

1. Add spring-boot-starter-jersey dependency.
2. Register JAX-RS resource (annotated with JAX-RS annotation) as a bean.
3. Register the resource class, created in step 2, with `org.glassfish.jersey.server.ResourceConfig`. The instance of `ResourceConfig` is itself needed to be registered as Spring bean.

# Example

#### pom.xml

```
<project .....>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.logicbig.example</groupId>
    <artifactId>using-jax-rs-in-boot-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.0.RELEASE</version>
    </parent>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jersey</artifactId>
        </dependency>
    </dependencies>
</project>
```

## The JAX-RS Resource Class

```
package com.logicbig.example;

import org.springframework.stereotype.Component;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component
@Path("/app")
public class MyJaxRsResource {

  MyJaxRsResource(){
      System.out.println(this);
  }

  @GET
  @Path("/{type}")
  public String getMessage(@PathParam("type") String type) {
      return "message from JAX-RS app for " + type;
  }
}
```

## Registering the JAX-RS resource

```
package com.logicbig.example;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringBootMain {

  @Bean
  ResourceConfig resourceConfig() {
      return new ResourceConfig().register(MyJaxRsResource.class);
  }

  public static void main(String[] args) {
      SpringApplication.run(SpringBootMain.class);
  }
}
```