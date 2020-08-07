package list.single;

public class Example {
    public static void main(String[] args) {

    }


    /**
     * 插入头节点
     */
    public static void insertHead(Node head, Node newNode){
        Node old = head;
        head = newNode;
        head.next = old;
    }

    /**
     * 插入尾节点
     */
    public static void insertTail(Node tail, Node newTail){
        Node old = tail;
        tail = newTail;
        tail.next = null;
        old.next = tail;
    }

    /**
     * 遍历
     */
    public static void traverse(Node head){
        while (head !=null){
            System.out.println(head.value + "");
            head = head.next;
        }
        System.out.println();
    }



}
