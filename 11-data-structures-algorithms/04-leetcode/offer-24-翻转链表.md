# Offer-24-翻转链表

https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/

> 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
>
> 示例:
>
> 输入: 1->2->3->4->5->NULL
> 输出: 5->4->3->2->1->NULL
>
>
> 限制：
>
> 0 <= 节点个数 <= 5000
>

```java
    /**
     * 翻转
     * 时间复杂度O(n), 空间复杂度O(1)
     */
    public static Node reverseList(Node head) {
        //当前节点的上一个节点
        Node pre = null;
        // 当前节点的下一个节点
        Node next = null;
        while (head != null) {
            //记录一下下一个节点的位置
            next = head.next;
            //将当前节点的尾指针指向它前一个位置
            head.next = pre;
            //往后移动一位
            pre = head;
            //往后移动一位
            head = next;
        }
        return pre;
    }
```

迭代法

好理解的双指针

- 定义两个指针： pre 和 cur ；pre 在前 cur在后。
- 每次让 pre 的 next 指向 cur ，实现一次**局部反转**
- 局部反转完成之后， pre 和 cur 同时往前移动一个位置
- 循环上述过程，直至 pre 到达链表尾部



![img](https://pic.leetcode-cn.com/9ce26a709147ad9ce6152d604efc1cc19a33dc5d467ed2aae5bc68463fdd2888.gif)

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode cur = null, pre = head, next = null;
        while(pre != null){
            next = pre.next;
            pre.next = cur;
            cur = pre;
            pre = next;
        }
        return cur;
    }
}
```

#### 递归

- 使用递归函数，一直递归到链表的最后一个结点，该结点就是反转后的头结点，记作 retret .
- 此后，每次函数在返回的过程中，**让当前结点的下一个结点的 next  指针指向当前节点。**
- 同时让当前结点的 next  指针指向 NULL ，从而实现从链表尾部开始的局部反转
- 当递归函数全部出栈后，链表反转完成。

![img](https://pic.leetcode-cn.com/8951bc3b8b7eb4da2a46063c1bb96932e7a69910c0a93d973bd8aa5517e59fc8.gif)

```java
    public ListNode reverseList(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        ListNode ret = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return ret;
    }
```

## 

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode cur = null, pre = head;
        while(pre != null){
            ListNode next = pre.next;
            pre.next = cur;
            cur = pre;
            pre =  next;
        }
        return cur;
    }
}
```

