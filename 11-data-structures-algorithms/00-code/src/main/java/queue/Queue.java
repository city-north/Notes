package queue;

public class Queue<E> {
    private Object[] data = null;
    private int maxSize;
    private int front;
    private int rear;

    public Queue() {
        this(10);
    }

    public Queue(int maxSize) {
        if (maxSize <0){
            throw new RuntimeException("can not be less than 0");
        }
        this.maxSize = maxSize;
        data = new Object[maxSize];
        front = rear = 0;
    }

    public boolean add(E e){
        if (rear == maxSize){
            throw new RuntimeException("already full");
        }
        data[rear ++] = e;
        return true;
    }


    public E poll(){
        if (empty()){
            throw new RuntimeException("already  empty");
        }else {
            E datum = (E)data[front];
            data[front++] = null;
            return datum;
        }
    }

    public E peek(){
        if (empty()){
            throw new RuntimeException("already  empty");
        }else {
            E datum = (E)data[front];
            return datum;
        }
    }

    public boolean empty(){
        return rear == front;
    }

    public static void main(String[] args) {
        Queue<Integer> integerQueue  = new Queue<Integer>(10);
        integerQueue.add(1);
        integerQueue.add(2);
        integerQueue.add(3);
        integerQueue.add(4);
        System.out.println(integerQueue.peek());
        System.out.println(integerQueue.poll());
        System.out.println(integerQueue.poll());
        System.out.println(integerQueue.poll());
        System.out.println(integerQueue.poll());

    }

}