# LinkedHashMap

参考：

https://www.jianshu.com/p/8f4f58b4b8ab

https://blog.csdn.net/zxt0601/article/details/77429150



## 简介

	`HashMap`有一个问题就是迭代是无序迭代，为了解决这个问题，`LinkedHashMap`通过维护一条双线链表保证元素迭代的顺序和插入时的顺序一致。

特点：

- 继承于`HashMap`，双向链表，插入顺序和访问顺序。
- 在每次**插入数据，或者访问、修改数据**时，**会增加节点、或调整链表的节点顺序**。以决定迭代时输出的顺序。
- `Key`和`value` 都允许为`null`

- `key`重复会覆盖，`value`允许重复
- 线程非安全



​	默认情况，遍历时的顺序是**按照插入节点的顺序**。也可以在构造时传入`accessOrder`参数，使得其遍历顺序**按照访问的顺序**输出。

​	`accessOrder=true`的模式下,在`afterNodeAccess()`函数中，会将当前被访问到的节点`e`，移动至内部的双向链表的尾部。值得注意的是，`afterNodeAccess()`函数中，会修改`modCount`,因此当你正在`accessOrder=true`的模式下,迭代`LinkedHashMap`时，如果同时查询访问数据，也会导致`fail-fast`，因为迭代的顺序已经改变。



下面代码展示了插入顺序与遍历顺序的区别：



```
 Map<String, String> map = new LinkedHashMap<>();
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");
        map.put("4", "d");

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("以下是accessOrder=true的情况:");

        map = new LinkedHashMap<String, String>(10, 0.75f, true);
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");
        map.put("4", "d");
        map.get("2");//2移动到了内部的链表末尾
        map.get("4");//4调整至末尾
        map.put("3", "e");//3调整至末尾
        map.put(null, null);//插入两个新的节点 null
        map.put("5", null);//5
        iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
```

输出：

```
1=a
2=b
3=c
4=d
以下是accessOrder=true的情况:
1=a
2=b
4=d
3=e
null=null
5=null
```



## 图示

![图示](assets/249993-20161215143120620-1544337380.png)