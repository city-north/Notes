package list.single;

public class Example {


    /**
     * 插入头节点
     */
    public static void insertHead(Node head, Node newNode) {
        //记录下原来节点的引用
        Node old = head;
        //头结点设置为新插入的及诶单
        head = newNode;
        //头结点的后续节点设置为老的元素
        head.next = old;
    }

    /**
     * 插入尾节点
     */
    public static void insertTail(Node tail, Node newTail) {
        Node old = tail;
        tail = newTail;
        tail.next = null;
        old.next = tail;
    }

    /**
     * 遍历
     */
    public static void traverse(Node head) {
        while (head != null) {
            System.out.println(head.value + "");
            head = head.next;
        }
        System.out.println();
    }


    /**
     * 遍历查找
     */
    public static int find(Node head, int value) {
        int index = -1;
        int count = 0;
        while (head != null) {
            if (head.value == value) {
                index = count;
                return index;
            }
            count++;
            head = head.next;
        }
        return index;
    }

    /**
     * 插入节点
     */
    public static void insert(Node p, Node s) {
        Node next = p.next;
        p.next = s;
        s.next = next;
    }


    /**
     * 删除
     */
    public static void delete(Node head, Node q) {
        if (q != null && q.next != null) {
            Node p = q.next;
            q.value = p.value;
            q.next = p.next;
            p = null;
        }
        //删除最后一个元素
        if (q != null && q.next == null) {
            while (head != null) {
                if (head.next != null && head.next == q) {
                    head.next = null;
                    break;
                }
                head = head.next;
            }
        }

    }

    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        node1.next = node2;
        node2.next = node3;
        node3.next = null;

        traverse(node1);
        Node newHead = new Node(0);
        insertHead(node1, newHead);
        traverse(newHead);

        Node newTail = new Node(4);
        insertTail(node3, newTail);
        traverse(newHead);

        //验证查找
        System.out.println(find(newHead, 3));

        //
        insert(node3,new Node(99));
        traverse(newHead);

        //删除
        delete(newHead,node3);
        traverse(newHead);

    }
}
