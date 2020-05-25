# Feign 的 Post和 Get 的多参数传递

在 web 开发中,SPringMVC 支持 GET 方法直接绑定 pojo 的,但是 Feign 的实现并未覆盖所有的 SpringMVC 的功能,目前解决方法有多个,最常见的解决办法如下

- 将 POJO 拆散成一个个单独的属性放在方法参数里
- 把方法参数编程 Map 传递
- 使用 GET 传递@RequestBody ,但是违反了 Restful

本案例给出最佳实践方式,通过 Feign 拦截器方式处理

#### 实现拦截器接口

通过 Feign 的 RequestInterceptor 中的 apply 方法来进行统一拦截转换处理 Feign 中的 GET 方法多参数传递的问题,

Feign 进行 POST 多参数传递相比进行 GET 多传递要来的简单

```java
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void apply(RequestTemplate template) {
        // feign 不支持 GET 方法传 POJO, json body转query
        if (template.method().equals("GET") && template.body() != null) {
            try {
                JsonNode jsonNode = objectMapper.readTree(template.body());
                template.body(null);

                Map<String, Collection<String>> queries = new HashMap<>();
                buildQuery(jsonNode, "", queries);
                template.queries(queries);
            } catch (IOException e) {
                //提示:根据实践项目情况处理此处异常，这里不做扩展。
                e.printStackTrace();
            }
        }
    }

    private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {
        if (!jsonNode.isContainerNode()) {   // 叶子节点
            if (jsonNode.isNull()) {
                return;
            }
            Collection<String> values = queries.get(path);
            if (null == values) {
                values = new ArrayList<>();
                queries.put(path, values);
            }
            values.add(jsonNode.asText());
            return;
        }
        if (jsonNode.isArray()) {   // 数组节点
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), path, queries);
            }
        } else {
            Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (StringUtils.hasText(path)) {
                    buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);
                } else {  // 根节点
                    buildQuery(entry.getValue(), entry.getKey(), queries);
                }
            }
        }
    }
}

```

## 统一 Token 拦截器

```java
/**
 * Feign统一Token拦截器
 */
@Component
public class FeignTokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(null==getHttpServletRequest()){
            //此处省略日志记录
            return;
        }
        //将获取Token对应的值往下面传
        requestTemplate.header("oauthToken", getHeaders(getHttpServletRequest()).get("oauthToken"));
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Feign拦截器拦截请求获取Token对应的值
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}

```

