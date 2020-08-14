package stack;


/**
 * <p>
 * 栈的接口
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/08/14 21:45
 */
public interface Stack<T> {

    /**
     * 弹出
     *
     * @return ListNode
     */
     T pop();


    /**
     * 压栈
     *
     * @param item 栈 node
     */
    void push(T item);


    /**
     * @return 栈深度
     */
    int size();


    /**
     * @return 是否为空
     */
    boolean isEmpty();

}
