# Jedis

## 特点

Jedis 是我们最熟悉和最常用的客户端。轻量，简洁，便于集成和改造。

```
public static void main(String[] args) {
	Jedis jedis = new Jedis("127.0.0.1", 6379); 
	jedis.set("qingshan", "2673"); 
	System.out.println(jedis.get("qingshan")); 
	jedis.close();
}
```

