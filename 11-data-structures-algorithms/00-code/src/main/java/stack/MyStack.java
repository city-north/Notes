package stack;


import list.ListNode;


/**
 * 链表实现的栈
 * stack.MyStack
 */
public class MyStack implements Stack<Integer> {

    /**
     * 栈顶
     */
    private ListNode first;
    /**
     * 栈深度
     */
    private int size;


    /**
     * 弹出
     *
     * @return ListNode
     */
    public Integer pop() {
        int value = first.value;
        first = first.next;
        size--;
        return value;
    }

    /**
     * 压栈
     *
     * @param item 栈 node
     */
    public void push(Integer item) {
        ListNode oldFirst = first;
        ListNode itemNode = new ListNode(item);
        first = itemNode;
        itemNode.next = oldFirst;
        size++;
    }

    /**
     * @return 栈深度
     */
    public int size() {
        return size;
    }

    /**
     * @return 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());

    }
}
