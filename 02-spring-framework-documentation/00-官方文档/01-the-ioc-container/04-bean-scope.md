

## Request, Session, Application, and WebSocket Scopes

##### Application Scope

每一个 ServletContext 一个实例

注解

```
@ApplicationScope
@Component
public class AppPreferences {
    // ...
}
```

xml:

```xml
<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>
```

