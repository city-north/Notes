# 040-Request-Bean作用域

在一次 HTTP 请求中,每个 Bean 定义对应一个实例,该作用域进在基于 Web 的 Sprng上下文(例如 SpringMVC)中才有效

注解:

```java
@RequestScope
@Component
public class LoginAction {
    // ...
}
```

xml:

```xml
<bean id="loginAction" class="com.something.LoginAction" scope="request"/>
```

##### 

```java
/**
 * Request-backed {@link org.springframework.beans.factory.config.Scope}
 * implementation.
 */
public class RequestScope extends AbstractRequestAttributesScope {

	@Override
	protected int getScope() {
		return RequestAttributes.SCOPE_REQUEST;
	}

	/**
	 * There is no conversation id concept for a request, so this method
	 * returns {@code null}.
	 */
	@Override
	@Nullable
	public String getConversationId() {
		return null;
	}
}
```

