package stack;

import list.single.ListNode;

import javax.xml.soap.Node;
import java.util.List;

public class MyStack {
    private ListNode stackTop;
    private ListNode stackBottom;

    public MyStack(ListNode stackTop, ListNode stackBottom) {
        this.stackTop = stackTop;
        this.stackBottom = stackBottom;
        this.stackTop = this.stackBottom;
    }


    /**
     * 压栈
     */
    public static void pushStack(MyStack stack, int value) {
        ListNode node = new ListNode(value);
        node.next = stack.stackTop;
        stack.stackTop = node;
    }

    /**
     * 遍历
     */
    public static void traverse(MyStack stack) {
        ListNode stackTop = stack.stackTop;
        while (stackTop != stack.stackBottom) {
            System.out.print(stackTop.value + " ");
            stackTop = stackTop.next;
        }
        System.out.println();
    }


    /**
     * 判断是否为空
     */
    public static boolean isEmpty(MyStack stack) {
        return stack.stackTop == stack.stackBottom;
    }

    /**
     * 出栈
     */
    public static void pop(MyStack myStack) {
        if (isEmpty(myStack)) {
            return;
        }
        ListNode stackTop = myStack.stackTop;
        myStack.stackTop = stackTop.next;
        System.out.println(stackTop.value);
    }

    public static void clear(MyStack stack){
        stack.stackTop = null;
        stack.stackBottom = null;
    }

    public static void main(String[] args) {
        MyStack stack  = new MyStack(new ListNode(0),new ListNode(0));
        System.out.println(isEmpty(stack));
//        traverse(stack);
        pushStack(stack,1);
        pushStack(stack,2);
        pushStack(stack,3);
        traverse(stack);
        pop(stack);
        pop(stack);
        pop(stack);
    }

}
