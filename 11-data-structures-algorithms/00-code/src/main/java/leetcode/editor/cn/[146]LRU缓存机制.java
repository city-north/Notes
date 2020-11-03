package leetcode.editor.cn;
//运用你所掌握的数据结构，设计和实现一个 LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。
//
// 获取数据 get(key) - 如果关键字 (key) 存在于缓存中，则获取关键字的值（总是正数），否则返回 -1。 
//写入数据 put(key, value) - 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字/值」。当缓存容量达到上限时，它应该在
//写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。 
//
// 
//
// 进阶: 
//
// 你是否可以在 O(1) 时间复杂度内完成这两种操作？ 
//
// 
//
// 示例: 
//
// LRUCache cache = new LRUCache( 2 /* 缓存容量 */ );
//
//cache.put(1, 1);
//cache.put(2, 2);
//cache.get(1);       // 返回  1
//cache.put(3, 3);    // 该操作会使得关键字 2 作废
//cache.get(2);       // 返回 -1 (未找到)
//cache.put(4, 4);    // 该操作会使得关键字 1 作废
//cache.get(1);       // 返回 -1 (未找到)
//cache.get(3);       // 返回  3
//cache.get(4);       // 返回  4
// 
// Related Topics 设计 
// 👍 974 👎 0


//1
//2 1
//1 2
//3 1  | 2
//4 3  | 1
//3 4
//4 3


import java.util.HashMap;

//leetcode submit region begin(Prohibit modification and deletion)
class LRUCache {


    private int capacity;
    private CacheNode tail = new CacheNode(-1, -1);
    private CacheNode head = new CacheNode(-1, -1);
    private HashMap<Integer, CacheNode> valNodeMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        tail.prev = head;
        head.next = tail;
        valNodeMap = new HashMap<Integer, CacheNode>(capacity);
    }

    public int get(int key) {
        if (!valNodeMap.containsKey(key)) {
            return -1;

        }
        final CacheNode cacheNode = valNodeMap.get(key);
        synchronized (valNodeMap) {
            cacheNode.prev.next = cacheNode.next;
            cacheNode.next.prev = cacheNode.prev;
            moveToTail(cacheNode);
        }
        return cacheNode.value;
    }

    public void put(int key, int value) {
        synchronized (valNodeMap) {
            if (get(key) != -1) {
                valNodeMap.get(key).value = value;
                return;
            }
            if (valNodeMap.size() >= capacity) {
                valNodeMap.remove(head.next.key);
                head.next = head.next.next;
                head.next.prev = head;
            }
            CacheNode cacheNode = new CacheNode(key, value);
            valNodeMap.put(key, cacheNode);
            moveToTail(cacheNode);
        }
    }

    private void moveToTail(CacheNode cacheNode) {
        cacheNode.prev = tail.prev;
        tail.prev = cacheNode;
        cacheNode.prev.next = cacheNode;
        cacheNode.next = tail;
    }

    private class CacheNode {
        CacheNode prev;
        CacheNode next;
        int key;
        int value;

        public CacheNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
//leetcode submit region end(Prohibit modification and deletion)
