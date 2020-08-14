package stack;


/**
 * 数组实现的栈
 * @param <E>
 */
public class StackFromArrayList<E> {
    /**
     * 数组实现
     */
    private Object[] data = null;
    /**
     * 栈底
     */
    private int maxSize = 0;
    /**
     * 栈顶
     */
    private int top = -1;

    public StackFromArrayList() {
        this(10);
    }

    public StackFromArrayList(int maxSize) {
        assert maxSize >=0;
        this.maxSize = maxSize;
        data  = new Object[maxSize];
        top = -1;
    }




    public boolean push(E e){
        if (top ==maxSize -1){
            throw new RuntimeException("The stack is already full");
        }else {
            data[++top] = e;
            return true;
        }
    }


    public E  pop(){
        if (top== -1){
            throw new RuntimeException("The stack is already empty");
        }else {
            return (E)data[top--];
        }
    }

    public E peek(){
        if (top== -1){
            throw new RuntimeException("The stack is already empty");
        }else {
            return (E)data[top];
        }
    }

    public static void main(String[] args) {
        StackFromArrayList<Integer> stack = new StackFromArrayList(11);
        int q=1,w=2,e=3,r=4;
        System.out.println(stack.push(q));
        System.out.println(stack.push(w));
        System.out.println(stack.push(e));
        System.out.println(stack.push(r));
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }




}
