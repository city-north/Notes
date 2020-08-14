package list.twoWayLinkedList;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/14 22:13
 */
public class TwoWayLinkedList {


    private class Node {
        Node prev;
        Node next;
        Object data;

        public Node(Object data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int length;

    public TwoWayLinkedList() {
        length = 0;
        head = tail = null;
    }


    public void addHead(Object value) {
        Node newHead = new Node(value);
        if (length == 0) {
            head = newHead;
            tail = newHead;
            length++;
        } else {
            head.prev = newHead;
            newHead.next = head;
            head = newHead;
            length++;

        }


        Node oldHead = head;
        head.next = oldHead;
        length++;
    }
}
