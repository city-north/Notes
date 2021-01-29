# 03-LRU算法

实现 LRU 算法除了需要 key/value 字典外,还需要附加一个链表,链表中的元素按照一定的顺序进行排列

- 当空间满的时候,会踢掉链表尾部的元素
- 当字典的某个元素被访问时,它在链表的位置会被移动到表头,所以链表的元素排列顺序就是元素最近被访问的时间顺序
- 位于链表尾部的元素就是不被重用的元素,所以会被踢掉, 位于表头的元素就是最近刚刚被人用的元素,所以暂时不会被剔

## 实现一个 LRU 算法

使用 HashMap kv 存储的形式存储 LRUNode 

#### LRUNode

```java
    // 链表中的节点
    class LRUNode {
        String key;
        Object value;
        LRUNode prev;
        LRUNode next;
        public LRUNode(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
```

## 全部源码

```java
public class LRUCache {
    // KV形式存储缓存
    private HashMap<String, LRUNode> map;
    private int capacity; // 链表容量
    private LRUNode head; // 头结点
    private LRUNode tail; // 尾节点

    public void set(String key, Object value) {
        // 设置值，节被被访问时，移除节点，放到队头
        LRUNode node = map.get(key);
        if (node != null) {
            node = map.get(key);
            node.value = value;
            remove(node, false);
        } else {
            node = new LRUNode(key, value);
            if (map.size() >= capacity) {
                // 每次容量不足时先删除最久未使用的元素
                remove(tail, true);
            }
            map.put(key, node);
        }
        // 将刚添加的元素设置为head
        setHead(node);
    }

    // 取值，节被被访问时，移除节点，放到队头
    public Object get(String key) {
        LRUNode node = map.get(key);
        if (node != null) {
            // 将刚操作的元素放到head
            remove(node, false);
            setHead(node);
            return node.value;
        }
        return null;
    }

    // 将节点设置为头节点
    private void setHead(LRUNode node) {
        // 先从链表中删除该元素
        if (head != null) {
            node.next = head;
            head.prev = node;
        }
        head = node;
        if (tail == null) {
            tail = node;
        }
    }

    // 从链表中删除此Node，需注意该Node是head或者是tail的情形
    private void remove(LRUNode node, boolean flag) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        node.next = null;
        node.prev = null;
        if (flag) {
            map.remove(node.key);
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<String, LRUNode>();
    }


}
```

