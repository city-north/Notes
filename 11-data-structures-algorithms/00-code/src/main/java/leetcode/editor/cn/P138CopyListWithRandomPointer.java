//给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random ，该指针可以指向链表中的任何节点或空节点。 
//
// 构造这个链表的 深拷贝。 深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。新节点的 next 指针和 random 
//指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。复制链表中的指针都不应指向原链表中的节点 。 
//
// 例如，如果原链表中有 X 和 Y 两个节点，其中 X.random --> Y 。那么在复制链表中对应的两个节点 x 和 y ，同样有 x.random 
//--> y 。 
//
// 返回复制链表的头节点。 
//
// 用一个由 n 个节点组成的链表来表示输入/输出中的链表。每个节点用一个 [val, random_index] 表示： 
//
// 
// val：一个表示 Node.val 的整数。 
// random_index：随机指针指向的节点索引（范围从 0 到 n-1）；如果不指向任何节点，则为 null 。 
// 
//
// 你的代码 只 接受原链表的头节点 head 作为传入参数。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
//输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [[1,1],[2,1]]
//输出：[[1,1],[2,1]]
// 
//
// 示例 3： 
//
// 
//
// 
//输入：head = [[3,null],[3,0],[3,null]]
//输出：[[3,null],[3,0],[3,null]]
// 
//
// 示例 4： 
//
// 
//输入：head = []
//输出：[]
//解释：给定的链表为空（空指针），因此返回 null。
// 
//
// 
//
// 提示： 
//
// 
// 0 <= n <= 1000 
// -10000 <= Node.val <= 10000 
// Node.random 为空（null）或指向链表中的节点。 
// 
// Related Topics 哈希表 链表 
// 👍 495 👎 0


package leetcode.editor.cn;

import java.util.HashMap;

/**
 * 138：复制带随机指针的链表
 *
 * @author EricChen
 */
public class P138CopyListWithRandomPointer {
    public static void main(String[] args) {
        Solution solution = new P138CopyListWithRandomPointer().new Solution();
        // TO TEST
    }

    //leetcode submit region begin(Prohibit modification and deletion)
// Definition for a Node.
    class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    class Solution {
        public Node copyRandomList(Node head) {
            HashMap<Node, Node> map = new HashMap<>();
            Node newHead = head;
            //random
            while (newHead != null) {
                if (!map.containsKey(newHead)) {
                    map.put(newHead, new Node(newHead.val));
                }
                final Node random = newHead.random;
                if (random != null) {
                    if (!map.containsKey(random)) {
                        map.put(random, new Node(random.val));
                    }
                    map.get(newHead).random = map.get(random);
                }
                newHead = newHead.next;
            }
            //next
            newHead = head;
            while (newHead != null) {
                Node next = newHead.next;
                map.get(newHead).next = map.get(next);
                newHead = newHead.next;
            }
            return map.get(head);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)
//Map<Node,Node> map = new HashMap<>();
//    Node newHead = head;
//    //遍历一遍,将 val取出
//        while(newHead != null) {
//        if(!map.containsKey(newHead)){
//            map.put(newHead,new Node(newHead.val));
//        }
//        if(newHead.random != null) {
//            Node random = newHead.random;
//            if(!map.containsKey(random)) {
//                map.put(random,new Node(random.val));
//            }
//            map.get(newHead).random = map.get(random)	;
//        }
//        newHead = newHead.next;
//    }
//    newHead = head;
//        while (newHead != null){
//        Node next = newHead.next;
//        map.get(newHead).next = map.get(next);
//        newHead = newHead.next;
//    }
//        return map.get(head);
}