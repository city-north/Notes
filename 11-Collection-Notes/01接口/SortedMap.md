# SortedMap

[`SortedMap`](https://docs.oracle.com/javase/8/docs/api/java/util/SortedMap.html)是 [`Map`](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)按升序维护其条目，根据键的自然顺序排序，或根据创建`Comparator`时提供的条目排序的`SortedMap`。

```
public interface SortedMap<K, V> extends Map<K, V>{
    Comparator<? super K> comparator();//接收比较器，用于Map排序
    SortedMap<K, V> subMap(K fromKey, K toKey);//k1，k2之间的键值对
    SortedMap<K, V> headMap(K toKey);//在k之前的键值对
    
    SortedMap<K, V> tailMap(K fromKey);//集合最后的键值对
    K firstKey();//第一个key
    K lastKey();//最后的key
    
    Set<Map.Entry<K, V>> entrySet();//后去Map中的entrySet集合
    Set<K> keySet();//获取key的set集合
    Collection<V> values();//获取key的set集合
}
```

可以看出，`SortedMap`是精准模拟版的`SortedSet`

## Map操作

这些操作`SortedMap`从`Map`有序映射和法线贴图上的行为继承，但有两个例外：

- 在`Iterator`由返回`iterator`任何的有序映射的操作`Collection`，以便意见遍历集合。
- `Collection`视图`toArray`操作返回的数组按顺序包含键，值或条目。

尽管它不是由接口保证的，所述`toString`的方法`Collection`中的所有Java平台的观点`SortedMap`实现返回包含视图的所有元素中，为了一个字符串。

