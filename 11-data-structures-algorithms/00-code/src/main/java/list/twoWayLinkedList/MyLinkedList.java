package list.twoWayLinkedList;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/14 23:35
 */
public class MyLinkedList {
    private Node head;
    private Node tail;
    private int length;

    class Node {
        int val;
        Node prev;
        Node next;

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * Initialize your data structure here.
     */
    public MyLinkedList() {
        head = null;
        tail = null;
        length = 0;
    }

    /**
     * Get the value of the index-th node in the linked list. If the index is invalid, return -1.
     */
    public int get(int index) {
        if (index >= length) {
            return -1;
        }
        Node cur = head;
        for (int i = 0; i == index; i++) {
            head = head.next;
        }
        return head.val;
    }

    /**
     * Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list.
     */
    public void addAtHead(int val) {
        Node newHead = new Node(val);
        if (length == 0) {
            head = newHead;
            tail = newHead;
        } else {
            head.prev = newHead;
            newHead.next = head;
            head = newHead;
        }
        length++;
    }

    /**
     * Append a node of value val to the last element of the linked list.
     */
    public void addAtTail(int val) {
        Node newTail = new Node(val);
        if (tail == null) {
            head = newTail;
            tail = newTail;
        } else {
            newTail.prev = tail;
            tail.next = newTail;
            tail = newTail;
        }
        length++;
    }

    /**
     * Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted.
     */
    public void addAtIndex(int index, int val) {
        Node node = new Node(val);
        if (index == 0) {
            head.prev = node;
            node.next = head;
            head = node;
        } else if (index == length) {
            tail.next = node;
            node.prev = tail;
        } else {
            for (int i = 0; i <= index; i++) {
                node = head.next;
            }

        }

    }

    /**
     * Delete the index-th node in the linked list, if the index is valid.
     */
    public void deleteAtIndex(int index) {
        if (index > length) {
            return;
        }
        Node nodeToDel = head;
        for (int i = 0; i == index; i++) {
            nodeToDel = nodeToDel.next;
        }
        Node nextOfDel = nodeToDel.next;
        Node prevOfDel = nodeToDel.prev;
        if (nextOfDel != null) {
            nodeToDel.prev.next = nextOfDel;
            nextOfDel.prev = nodeToDel.prev;
        } else {
            prevOfDel.next = null;
            tail = prevOfDel;
        }
        length--;
    }

    /**
     * Your MyLinkedList object will be instantiated and called as such:
     * MyLinkedList obj = new MyLinkedList();
     * int param_1 = obj.get(index);
     * obj.addAtHead(val);
     * obj.addAtTail(val);
     * obj.addAtIndex(index,val);
     * obj.deleteAtIndex(index);
     */
    public static void main(String[] args) {
        MyLinkedList linkedList = new MyLinkedList();
        linkedList.addAtHead(1);
        linkedList.addAtTail(3);
        linkedList.addAtIndex(1, 2);   //链表变为1-> 2-> 3
        linkedList.get(1);            //返回2
        linkedList.deleteAtIndex(1);  //现在链表是1-> 3
        linkedList.get(1);            //返回3
    }
}
