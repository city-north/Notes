## Queue

`Queue`是一个用于处理之前保存元素的集合。

```
public interface Queue<E> extends Collection<E> {
    E element();
    boolean offer(E e);
    E peek();
    E poll();
    E remove();
}
```

`Queue`方法存在两种形式：

- 在操作失败时抛出异常
- 在操作失败时抛出一个特殊值（`null`或者`false`）



| 操作类型 | 报错时抛出异常 | 报错时返回特殊值 |
| -------- | -------------- | ---------------- |
| 插入     | `add(e)`       | `offer(e)`       |
| 移除     | `remove()`     | `poll()`         |
| 检查     | `element()`    | `peek()`         |

- add

`Queue`继承自`Collection`，在不违反容器容量限制的情况下插入元素，如果违反，抛出`IllegalStateException`

- offer

intended solely for use on bounded queues（仅限于有界队列），如果插入失败，会返回false.

- remove and poll

都是移出元素并返回队列的头，当队列是empty时，`remove`会抛出`NoSuchElementException`，而`poll`返回`null`.

- The `element` and `peek` 

方法返回队列头，但是不删除，当队列是empty时，`element`会抛出`NoSuchElementException`，而`peek`返回`null`.

`Queue`不允许插入`null`作为元素。但是其实现类`LinkedList`，是一个改装实现了`Queue`是一个例外，因为历史原因，它支持`null`作为元素，但你要慎重使用，因为`null`是方法`pull`和`peek`方法的一个特殊返回值（具有其他意义）



## 例子

一秒钟打印一个字符：

```
import java.util.*;

public class Countdown {
    public static void main(String[] args) throws InterruptedException {
        int time = Integer.parseInt(args[0]);
        Queue<Integer> queue = new LinkedList<Integer>();

        for (int i = time; i >= 0; i--)
            queue.add(i);

        while (!queue.isEmpty()) {
            System.out.println(queue.remove());
            Thread.sleep(1000);
        }
    }
}
```

In the following example, a priority queue is used to sort a collection of elements. Again this program is artificial in that there is no reason to use it in favor of the `sort` method provided in `Collections`, but it illustrates the behavior of priority queues.

下面的例子，一个`priority queue`被用来将`Collection`中的元素排序。

```
static <E> List<E> heapSort(Collection<E> c) {
    Queue<E> queue = new PriorityQueue<E>(c);
    List<E> result = new ArrayList<E>();

    while (!queue.isEmpty())
        result.add(queue.remove());

    return result;
}
```