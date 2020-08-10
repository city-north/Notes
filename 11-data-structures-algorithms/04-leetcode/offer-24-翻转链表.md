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

```
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

